package com.example.scrlcanvas.ui.canvas.model

import androidx.compose.ui.geometry.Offset

data class SnapResult(
    val position: Offset,
    val lines: List<SnapLine>,
)