package com.bforbank.pokemon.ui.screens.pokemons.mapper

import com.bforbank.pokemon.domain.models.Pokemons
import com.bforbank.pokemon.ui.screens.pokemons.model.PokemonUiModel
import javax.inject.Inject

class PokemonsUiModelMapperImpl @Inject constructor() : PokemonsUiModelMapper {
    override fun map(pokemons: Pokemons): List<PokemonUiModel> {
        return pokemons.content?.map { pokemon ->
            PokemonUiModel(
                name = pokemon.name,
                id = pokemon.id,
            )
        } ?: emptyList()
    }
}
