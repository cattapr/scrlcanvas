package com.example.scrlcanvas.ui.canvas.state

import com.example.scrlcanvas.data.model.OverlayCategory
import com.example.scrlcanvas.domain.model.OverlaysDataResponse
import com.example.scrlcanvas.ui.canvas.model.PlacedCanvasItem

data class CanvasUiState(
    val showBottomSheet: Boolean = false,
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
    val overlays: OverlaysDataResponse? = null,
    val selectedCategory: OverlayCategory? = null,
    val selectedOverlays: List<PlacedCanvasItem> = emptyList(),
)