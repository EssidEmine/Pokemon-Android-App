package com.bforbank.pokemon.data.di

import com.bforbank.pokemon.data.repository.PokedexRepository
import com.bforbank.pokemon.data.repository.PokedexRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPokedexRepository(impl: PokedexRepositoryImpl): PokedexRepository
}
