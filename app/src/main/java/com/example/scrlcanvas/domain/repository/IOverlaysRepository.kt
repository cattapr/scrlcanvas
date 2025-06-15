package com.example.scrlcanvas.domain.repository

import com.example.scrlcanvas.domain.model.OverlaysDataResponse

interface IOverlaysRepository {
    suspend fun getOverlays(): Result<OverlaysDataResponse>
}
