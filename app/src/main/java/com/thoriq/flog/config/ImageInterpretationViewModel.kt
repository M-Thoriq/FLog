package com.thoriq.flog.config

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ImageInterpretationViewModel(
    private val generativeModel: GenerativeModel
) : ViewModel() {

    private val _uiState: MutableStateFlow<ImageInterpretationUiState> =
        MutableStateFlow(ImageInterpretationUiState.Initial)
    val uiState: StateFlow<ImageInterpretationUiState> =
        _uiState.asStateFlow()

    fun reason(
        userInput: String,
        selectedImages: List<Bitmap>
    ) {
        _uiState.value = ImageInterpretationUiState.Loading
        val prompt = """
        Analyze the provided image(s) and answer the following question: '$userInput'. 
        Please return the data in JSON format with the following fields:
        
        - name: A descriptive name for the identified object or concept.
        - short_description: A brief summary of the identified object or concept.
        - accuracy: An estimated accuracy percentage for the identification.
        
        Make sure the JSON structure is clean and easy to parse. Here is the expected output format:
        
        {
            "name": "string",
            "short_description": "string",
            "accuracy": accuracy percentage of the fish name
        }
        
        if its not a fish, then return the following:
        
        {
            "name": "Not a fish",
            "short_description": "The provided image(s) do not contain a fish.",
            "accuracy": 0
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
                        _uiState.value = ImageInterpretationUiState.Success(outputContent)
                    }
            } catch (e: Exception) {
                _uiState.value = ImageInterpretationUiState.Error(e.localizedMessage ?: "")
            }
        }
    }
}
