package com.thoriq.flog.config

/**
 * A sealed hierarchy describing the state of the text generation.
 */
sealed interface ImageInterpretationUiState {

    /**
     * Empty state when the screen is first shown
     */
    data object Initial: ImageInterpretationUiState

    /**
     * Still loading
     */
    data object Loading: ImageInterpretationUiState

    /**
     * Generated text
     */
    data class Success(
        val fish: String,
        val description: String,
        val avgWeight: String,
        val avgLength: String,
        val avgPrice: String
    ) : ImageInterpretationUiState

    /**
     * Error state when an error occurs
     */
    data class Error(val errorMessage: String): ImageInterpretationUiState
}
