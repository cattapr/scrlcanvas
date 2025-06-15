package com.example.scrlcanvas.ui.canvas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrlcanvas.data.model.OverlayCategory
import com.example.scrlcanvas.data.model.OverlayItem
import com.example.scrlcanvas.domain.usecases.IOverlaysUseCases
import com.example.scrlcanvas.ui.canvas.CanvasUiEvent
import com.example.scrlcanvas.ui.canvas.model.PlacedCanvasItem
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
constructor(
    private val overlaysUseCases: IOverlaysUseCases,
) : ViewModel() {
    private val _state = MutableStateFlow(CanvasUiState())
    val state = _state.asStateFlow()

    fun onEvent(event: CanvasUiEvent) {
        when (event) {
            CanvasUiEvent.OnToggleSheet -> toggleSheet()
            is CanvasUiEvent.OnSetOverlayCategory -> setOverlayCategory(event.category)
            is CanvasUiEvent.OnOverlaySelected -> addOverlayToCanvas(event.overlay)
        }
    }

    private fun addOverlayToCanvas(overlay: OverlayItem) {
        val newOverlay = PlacedCanvasItem(
            overlay = overlay
        )

        _state.update {
            it.copy(
                selectedOverlays = state.value.selectedOverlays + newOverlay,
                showBottomSheet = false
            )
        }
    }

    private fun setOverlayCategory(category: OverlayCategory) {
        _state.update { it.copy(selectedCategory = category) }
    }

    private fun toggleSheet() {
        viewModelScope.launch {
            _state.update { it.copy(showBottomSheet = !it.showBottomSheet) }
            if (state.value.showBottomSheet) {
                loadOverlays()
            }
        }
    }

    private fun loadOverlays() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            overlaysUseCases.getOverlays().fold(
                onSuccess = { overlays ->
                    _state.update {
                        it.copy(
                            overlays = overlays,
                            selectedCategory = overlays.first(),
                            hasError = false
                        )
                    }
                },
                onFailure = {
                    _state.update { it.copy(hasError = false) }
                }
            )
            _state.update { it.copy(isLoading = false) }
        }
    }
}