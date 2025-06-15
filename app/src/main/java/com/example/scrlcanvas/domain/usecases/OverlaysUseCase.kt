package com.example.scrlcanvas.domain.usecases

import javax.inject.Inject

interface IOverlaysUseCases {
    val getOverlays: IGetOverlaysUseCase
}

data class OverlaysUseCases
@Inject
constructor(
    override val getOverlays: IGetOverlaysUseCase,
) : IOverlaysUseCases
