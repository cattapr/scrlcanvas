package com.example.scrlcanvas.data.model

data class OverlayCategory(
    val title: String,
    val id: Int,
    val items: List<OverlayItem>,
    val thumbnail_url: String
)

data class OverlayItem(
    val id: Int,
    val overlay_name: String,
    val created_at: String,
    val category_id: Int,
    val source_url: String,
    val is_premium: Boolean,
    val includes_scrl_branding: Boolean,
    val premium_uses_last_48hrs: Int,
    val max_canvas_size: Int
)
