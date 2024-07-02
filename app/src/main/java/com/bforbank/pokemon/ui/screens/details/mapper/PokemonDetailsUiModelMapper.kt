package com.bforbank.pokemon.ui.screens.details.mapper

import com.bforbank.pokemon.domain.models.PokemonDetails
import com.bforbank.pokemon.ui.screens.details.model.PokemonDetailsContentUiModel

interface PokemonDetailsUiModelMapper {

    fun map(pokemonDetails: PokemonDetails): PokemonDetailsContentUiModel
}
