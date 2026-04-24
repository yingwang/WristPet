package com.wristpet.ui.screen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.wristpet.data.model.Pet
import com.wristpet.data.repository.PetRepository
import com.wristpet.engine.PetEngine
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PetViewModel(app: Application) : AndroidViewModel(app) {

    val pet: StateFlow<Pet?> = PetRepository.observe()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    init {
        viewModelScope.launch {
            PetRepository.get()
        }
    }

    fun onStepsUpdated(steps: Int) {
        viewModelScope.launch {
            val current = PetRepository.get()
            val updated = PetEngine.tick(current, steps)
            PetRepository.update(updated)
        }
    }

    fun onPetTapped() {
        viewModelScope.launch {
            val current = PetRepository.get()
            val updated = PetEngine.interact(current)
            PetRepository.update(updated)
        }
    }
}
