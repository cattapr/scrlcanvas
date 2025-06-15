package com.example.scrlcanvas.data.repository

import com.example.scrlcanvas.data.remote.IOverlaysRemoteDataSource
import com.example.scrlcanvas.domain.model.OverlaysDataResponse
import com.example.scrlcanvas.domain.repository.IOverlaysRepository
import javax.inject.Inject

class OverlaysRepository
@Inject
constructor(
    private val api: IOverlaysRemoteDataSource
) : IOverlaysRepository {
    override suspend fun getOverlays(): Result<OverlaysDataResponse> = api.getOverlays()
}