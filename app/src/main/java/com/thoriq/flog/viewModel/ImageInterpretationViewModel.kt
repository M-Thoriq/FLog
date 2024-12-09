package com.thoriq.flog.viewModel

import android.graphics.Bitmap
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
    var avgWeight: String = ""
    var avgLength: String = ""
    var avgPrice: String = ""

    fun reason(
        userInput: String,
        selectedImages: List<Bitmap>
    ) {
        _uiState.value = ImageInterpretationUiState.Loading
        val prompt = """
            
        Analyze the provided image(s) and answer the following question: '$userInput'. 
        Please return the data in JSON format with the following fields:
        
        - name: A descriptive name for the identified object or concept.
        - short_description: A brief summary of the identified object or concept, minimal 4 text.
        - avg_weight: The average weight of the identified object or concept in grams (only numbers, spaces, "-" and "gr").
        - avg_length: The average length of the identified object or concept in centimeters (only numbers, spaces, "-" and "cm").
        - avg_price: The average price of the identified object or concept in the market in Indonesian Rupiahs (only numbers, ".", spaces, "-" and "Rp." at the beginning).
        
        Make sure the JSON structure is clean and easy to parse. Here is the expected output format:
        
        {
            "name": "string",
            "short_description": "string",
            "avg_weight": "string",
            "avg_length": "string",
            "avg_price": "string"
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
                        image(bitmap)
                    }
                    text(prompt)
                }

                var outputContent = ""

                generativeModel.generateContentStream(inputContent)
                    .collect { response ->
                        outputContent += response.text
                    }
                parseJsonAndAssignVariables(outputContent)
                _uiState.value = ImageInterpretationUiState.Success(fish, description, avgWeight, avgLength, avgPrice)
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
            avgWeight = jsonObject.optString("avg_weight", "Unknown")
            avgLength = jsonObject.optString("avg_length", "Unknown")
            avgPrice = jsonObject.optString("avg_price", "Unknown")

            // Optionally, you can also extract the image link if you need it


        } catch (e: Exception) {
            // Handle any parsing errors
            _uiState.value = ImageInterpretationUiState.Error("Failed to parse JSON: ${e.localizedMessage}")
            println(_uiState.value)

        }
    }


}
