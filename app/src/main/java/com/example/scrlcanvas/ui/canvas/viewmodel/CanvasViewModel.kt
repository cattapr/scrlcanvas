package com.example.scrlcanvas.ui.canvas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrlcanvas.ui.canvas.CanvasUiEvent
import com.example.scrlcanvas.ui.canvas.state.CanvasUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CanvasViewModel
@Inject
constructor() : ViewModel() {
    private val _state = MutableStateFlow(CanvasUiState())
    val state = _state.asStateFlow()

    fun onEvent(event: CanvasUiEvent) {
        when (event) {
            CanvasUiEvent.OnToggleSheet -> {
                viewModelScope.launch {
                    _state.update { it.copy(showBottomSheet = !it.showBottomSheet) }
                }
            }
        }
    }
}