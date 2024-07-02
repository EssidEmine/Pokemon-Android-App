package com.bforbank.pokemon.ui.di

import com.bforbank.pokemon.ui.screens.details.mapper.PokemonDetailsUiModelMapper
import com.bforbank.pokemon.ui.screens.details.mapper.PokemonDetailsUiModelMapperImpl
import com.bforbank.pokemon.ui.screens.pokemons.mapper.PokemonsUiModelMapper
import com.bforbank.pokemon.ui.screens.pokemons.mapper.PokemonsUiModelMapperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UiModelMapperModule {

    @Binds
    abstract fun bindPokemonsUiModelMapper(impl: PokemonsUiModelMapperImpl): PokemonsUiModelMapper

    @Binds
    abstract fun bindPokemonsDetailUiModelMapper(
        impl: PokemonDetailsUiModelMapperImpl
    ): PokemonDetailsUiModelMapper
}
