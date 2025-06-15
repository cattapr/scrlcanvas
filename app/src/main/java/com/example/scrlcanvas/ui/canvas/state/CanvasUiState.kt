package com.example.scrlcanvas.ui.canvas.state

import com.example.scrlcanvas.domain.model.OverlaysDataResponse

data class CanvasUiState(
    val showBottomSheet: Boolean = false,
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
    val overlays: OverlaysDataResponse? = null
)