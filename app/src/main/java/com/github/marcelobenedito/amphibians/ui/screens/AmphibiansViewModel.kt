package com.github.marcelobenedito.amphibians.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.github.marcelobenedito.amphibians.AmphibiansApplication
import com.github.marcelobenedito.amphibians.data.AmphibiansRepository
import com.github.marcelobenedito.amphibians.network.Amphibian
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface AmphibiansUiState {
    data class Success(val amphibians: List<Amphibian>) : AmphibiansUiState
    data object Loading : AmphibiansUiState
    data object Error : AmphibiansUiState
}

class AmphibiansViewModel(private val amphibiansRepository: AmphibiansRepository) : ViewModel() {

    /** The mutable State that stores the status of the most recent request */
    var amphibiansUiState: AmphibiansUiState by mutableStateOf(AmphibiansUiState.Loading)
        private set

    /**
     * Call getAmphibians() on init so we can display state immediately.
     */
    init {
        getAmphibians()
    }

    /**
     * Gets Amphibians information from the Amphibians API Retrofit service and updates the
     * [Amphibian] [List] [MutableList]
     */
    fun getAmphibians() {
        viewModelScope.launch {
            amphibiansUiState = try {
                AmphibiansUiState.Success(amphibiansRepository.getAmphibians())
            } catch (e: IOException) {
                AmphibiansUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as AmphibiansApplication)
                val amphibiansRepository = application.container.amphibiansRepository
                AmphibiansViewModel(amphibiansRepository)
            }
        }
    }
}