package com.bforbank.pokemon.ui.screens.details.mapper

import com.bforbank.pokemon.domain.models.PokemonDetails
import com.bforbank.pokemon.ui.screens.details.model.PokemonDetailsContentUiModel
import com.bforbank.pokemon.ui.screens.details.model.StatsUiModel
import java.util.Locale
import javax.inject.Inject

class PokemonDetailsUiModelMapperImpl @Inject constructor() : PokemonDetailsUiModelMapper {

    override fun map(pokemonDetails: PokemonDetails): PokemonDetailsContentUiModel {
        return with(pokemonDetails) {
            PokemonDetailsContentUiModel(
                imageUrls = listOf(
                    sprites.backDefault,
                    sprites.backShiny,
                    sprites.frontDefault,
                    sprites.frontShiny
                ),
                stats = stats.map {
                    StatsUiModel(
                        baseStat = it.baseStat,
                        effort = it.effort,
                        name = it.stat.name.uppercase(locale = Locale.getDefault())
                    )
                },
                height = height,
                weight = weight
            )
        }
    }
}
