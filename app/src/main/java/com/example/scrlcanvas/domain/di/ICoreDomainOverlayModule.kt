package com.example.scrlcanvas.domain.di

import com.example.scrlcanvas.domain.usecases.GetOverlaysUseCase
import com.example.scrlcanvas.domain.usecases.IGetOverlaysUseCase
import com.example.scrlcanvas.domain.usecases.IOverlaysUseCases
import com.example.scrlcanvas.domain.usecases.OverlaysUseCases
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ICoreDomainOverlayModule {
    @Binds
    @Singleton
    fun bindOverlayUseCases(overlaysUseCase: OverlaysUseCases): IOverlaysUseCases


    @Binds
    @Singleton
    fun bindGetOverlayUseCase(
        getOverlaysUseCase: GetOverlaysUseCase
    ): IGetOverlaysUseCase
}