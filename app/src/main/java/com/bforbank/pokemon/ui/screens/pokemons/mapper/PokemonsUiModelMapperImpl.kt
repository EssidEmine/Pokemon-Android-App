package com.bforbank.pokemon.ui.screens.pokemons.mapper

import com.bforbank.pokemon.domain.models.Pokemons
import com.bforbank.pokemon.ui.screens.pokemons.model.PokemonUiModel
import java.util.Locale
import javax.inject.Inject

class PokemonsUiModelMapperImpl @Inject constructor() : PokemonsUiModelMapper {
    override fun map(pokemons: Pokemons): List<PokemonUiModel> {
        return pokemons.content?.map { pokemon ->
            PokemonUiModel(
                name = pokemon.name.capitalize(Locale.getDefault()),
                id = pokemon.id,
            )
        }?.sortedByDescending { it.name }?.reversed()?.distinct() ?: emptyList()
    }
}
