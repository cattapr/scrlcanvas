package com.example.scrlcanvas.domain.usecases

import com.example.scrlcanvas.domain.model.OverlaysDataResponse
import com.example.scrlcanvas.domain.repository.IOverlaysRepository
import javax.inject.Inject

interface IGetOverlaysUseCase {
    suspend operator fun invoke(): Result<OverlaysDataResponse>
}


class GetOverlaysUseCase
@Inject
constructor(
    private val overlaysRepository: IOverlaysRepository
) : IGetOverlaysUseCase {
    override suspend fun invoke(): Result<OverlaysDataResponse> = overlaysRepository.getOverlays()
}
