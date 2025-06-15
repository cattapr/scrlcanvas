package com.example.scrlcanvas.data.di

import com.example.scrlcanvas.data.remote.IOverlaysRemoteDataSource
import com.example.scrlcanvas.data.remote.OverlaysRemoteDataSource
import com.example.scrlcanvas.data.repository.OverlaysRepository
import com.example.scrlcanvas.domain.repository.IOverlaysRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ICoreDataOverlayModule {
    @Binds
    @Singleton
    fun bindOverlayRemoteDataSource(service: OverlaysRemoteDataSource): IOverlaysRemoteDataSource

    @Binds
    @Singleton
    fun bindOverlaysRepository(repository: OverlaysRepository): IOverlaysRepository
}