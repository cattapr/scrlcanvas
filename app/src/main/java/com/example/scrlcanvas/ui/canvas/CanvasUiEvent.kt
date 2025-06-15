package com.example.scrlcanvas.ui.canvas

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.example.scrlcanvas.data.model.OverlayCategory
import com.example.scrlcanvas.data.model.OverlayItem

sealed class CanvasUiEvent {
    data object OnToggleSheet : CanvasUiEvent()
    data class OnSetOverlayCategory(val category: OverlayCategory) : CanvasUiEvent()
    data class OnOverlaySelected(val overlay: OverlayItem) : CanvasUiEvent()
    data class OnCanvasOverlayTapped(val id: String) : CanvasUiEvent()
    data object OnDeselectCanvasOverlays : CanvasUiEvent()
    data class OnCanvasOverlayPositionChange(
        val id: String,
        val dragAmount: Offset,
        val canvasSize: Size,
        val itemSize: Size
    ) : CanvasUiEvent()
}