package com.example.scrlcanvas.ui.canvas

import com.example.scrlcanvas.data.model.OverlayCategory

sealed class CanvasUiEvent {
    data object OnToggleSheet : CanvasUiEvent()
    data class OnSetOverlayCategory(val category: OverlayCategory) : CanvasUiEvent()
}