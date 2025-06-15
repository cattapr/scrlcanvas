package com.example.scrlcanvas.ui.canvas.model

import com.example.scrlcanvas.data.model.OverlayItem

data class PlacedCanvasItem(
    val overlay: OverlayItem,
    val isSelected: Boolean = false
)