package com.bforbank.pokemon.data.di

import com.bforbank.pokemon.data.mapper.PokemonDetailsMapper
import com.bforbank.pokemon.data.mapper.PokemonDetailsMapperImpl
import com.bforbank.pokemon.data.mapper.PokemonsMapper
import com.bforbank.pokemon.data.mapper.PokemonsMapperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MapperModule {

    @Binds
    abstract fun bindPokemonsMapper(impl: PokemonsMapperImpl): PokemonsMapper

    @Binds
    abstract fun bindPokemonDetailsMapper(impl: PokemonDetailsMapperImpl): PokemonDetailsMapper
}
