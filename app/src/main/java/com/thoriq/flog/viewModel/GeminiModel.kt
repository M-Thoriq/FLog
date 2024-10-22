package com.thoriq.flog.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import com.thoriq.flog.BuildConfig


val GeminiModel = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        viewModelClass: Class<T>,
        extras: CreationExtras
    ): T {
        val config = generationConfig {
            temperature = 0.7f
        }

        return with(viewModelClass) {
            when {
                isAssignableFrom(ImageInterpretationViewModel::class.java) -> {
                    // Initialize a GenerativeModel with the `gemini-pro-vision` AI model
                    // for multimodal text generation
                    val generativeModel = GenerativeModel(
                        modelName = "gemini-1.5-pro",
                        apiKey = BuildConfig.GEMINI,
                        generationConfig = config
                    )
                    ImageInterpretationViewModel(generativeModel)
                }

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${viewModelClass.name}")
            }
        } as T
    }
}