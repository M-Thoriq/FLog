package com.thoriq.flog.viewModel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.thoriq.flog.config.ImageInterpretationUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

class ImageInterpretationViewModel(
    private val generativeModel: GenerativeModel
) : ViewModel() {

    private val _uiState: MutableStateFlow<ImageInterpretationUiState> =
        MutableStateFlow(ImageInterpretationUiState.Initial)
    val uiState: StateFlow<ImageInterpretationUiState> =
        _uiState.asStateFlow()

    var fish: String = ""
    var description: String = ""

    fun reason(
        userInput: String,
        selectedImages: List<Bitmap?>
    ) {
        _uiState.value = ImageInterpretationUiState.Loading
        val prompt = """
            
        Analyze the provided image(s) and answer the following question: '$userInput'. 
        Please return the data in JSON format with the following fields:
        
        - name: A descriptive name for the identified object or concept.
        - short_description: A brief summary of the identified object or concept.
        
        Make sure the JSON structure is clean and easy to parse. Here is the expected output format:
        
        {
            "name": "string",
            "short_description": "string",
        }
        
        if its not a fish, then return the following:
        
        {
            "name": "Not a fish",
            "short_description": "The provided image(s) do not contain a fish."
        }
        
    """



        viewModelScope.launch(Dispatchers.IO) {
            try {
                val inputContent = content {
                    for (bitmap in selectedImages) {
                        if (bitmap != null) {
                            image(bitmap)
                        }
                    }
                    text(prompt)
                }

                var outputContent = ""

                generativeModel.generateContentStream(inputContent)
                    .collect { response ->
                        outputContent += response.text
                    }
                parseJsonAndAssignVariables(outputContent)
                _uiState.value = ImageInterpretationUiState.Success(fish, description)
            } catch (e: Exception) {
                _uiState.value = ImageInterpretationUiState.Error(e.localizedMessage ?: "")
            }
        }

    }
    private fun parseJsonAndAssignVariables(outputContent: String) {
        try {
            val cleanedJson = outputContent
                .replace("```json", "")  // Remove ```json if present
                .replace("```", "")      // Remove trailing ```
                .trim()
            // Parse the JSON response
            val jsonObject = JSONObject(cleanedJson)

            // Extract the name and short_description from the JSON
            fish = jsonObject.optString("name", "Unknown")
            description = jsonObject.optString("short_description", "No description available")

            // Optionally, you can also extract the image link if you need it


        } catch (e: Exception) {
            // Handle any parsing errors
            _uiState.value = ImageInterpretationUiState.Error("Failed to parse JSON: ${e.localizedMessage}")
            println(_uiState.value)

        }
    }


}
