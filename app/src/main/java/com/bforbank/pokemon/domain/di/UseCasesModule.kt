package com.bforbank.pokemon.domain.di

import com.bforbank.pokemon.data.repository.PokedexRepository
import com.bforbank.pokemon.domain.usecases.GetPokemonDetailsUseCaseImpl
import com.bforbank.pokemon.domain.usecases.GetPokemonsUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class UseCasesModule {

    @Provides
    fun provideGetPokemonsUseCase(
        pokedexRepository: PokedexRepository
    ): GetPokemonsUseCaseImpl {
        return GetPokemonsUseCaseImpl(pokedexRepository)
    }

    @Provides
    fun provideGetPokemonDetailsCase(
        pokedexRepository: PokedexRepository
    ): GetPokemonDetailsUseCaseImpl {
        return GetPokemonDetailsUseCaseImpl(pokedexRepository)
    }
}
