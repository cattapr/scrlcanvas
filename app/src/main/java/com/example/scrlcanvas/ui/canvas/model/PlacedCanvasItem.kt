package com.example.scrlcanvas.ui.canvas.model

import androidx.compose.ui.geometry.Offset
import com.example.scrlcanvas.data.model.OverlayItem

data class PlacedCanvasItem(
    val overlay: OverlayItem,
    val isSelected: Boolean = false,
    val position: Offset = Offset.Zero
)