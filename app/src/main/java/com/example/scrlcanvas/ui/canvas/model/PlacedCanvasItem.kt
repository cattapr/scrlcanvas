package com.example.scrlcanvas.ui.canvas.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.example.scrlcanvas.data.model.OverlayItem
import java.util.UUID

data class PlacedCanvasItem(
    val id: String = UUID.randomUUID().toString(),
    val overlay: OverlayItem,
    val isSelected: Boolean = false,
    val position: Offset = Offset.Zero,
    val size: Size? = null
)
