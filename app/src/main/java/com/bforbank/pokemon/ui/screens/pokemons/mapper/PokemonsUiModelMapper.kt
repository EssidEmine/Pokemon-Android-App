package com.bforbank.pokemon.ui.screens.pokemons.mapper

import com.bforbank.pokemon.domain.models.Pokemons
import com.bforbank.pokemon.ui.screens.pokemons.model.PokemonUiModel

interface PokemonsUiModelMapper {
    fun map(pokemons: Pokemons): List<PokemonUiModel>
}
