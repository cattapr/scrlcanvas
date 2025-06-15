package com.example.scrlcanvas.data.remote

import com.example.scrlcanvas.domain.model.OverlaysDataResponse
import javax.inject.Inject

interface IOverlaysRemoteDataSource {
    suspend fun getOverlays(): Result<OverlaysDataResponse>
}


class OverlaysRemoteDataSource @Inject constructor(
    private val api: OverlaysApi
) : IOverlaysRemoteDataSource {

    override suspend fun getOverlays(): Result<OverlaysDataResponse> =
        try {
            val response = api.getOverlays()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("API error: ${response.code()}"))
            }
        } catch (error: Exception) {
            Result.failure(error)
        }
}