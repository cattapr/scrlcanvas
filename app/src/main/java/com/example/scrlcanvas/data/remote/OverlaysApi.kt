package com.example.scrlcanvas.data.remote

import com.example.scrlcanvas.domain.model.OverlaysDataResponse
import retrofit2.Response
import retrofit2.http.GET

interface OverlaysApi {
    @GET("scrl/test/overlays")
    suspend fun getOverlays(): Response<OverlaysDataResponse>
}