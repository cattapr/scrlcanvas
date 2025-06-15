package com.example.scrlcanvas.ui.canvas

sealed class CanvasUiEvent {
    data object OnToggleSheet : CanvasUiEvent()
}