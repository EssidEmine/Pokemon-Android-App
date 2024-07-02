package com.bforbank.pokemon.ui.screens.details.mapper

import com.bforbank.pokemon.domain.models.PokemonDetails
import com.bforbank.pokemon.ui.screens.details.model.PokemonDetailsContentUiModel
import com.bforbank.pokemon.ui.screens.details.model.StatsUiModel
import org.junit.Assert.assertEquals
import org.junit.Test

class PokemonDetailsUiModelMapperImplTest {

    private var mapper = PokemonDetailsUiModelMapperImpl()

    @Test
    fun `map should return PokemonDetailsContentUiModel`() {
        // Arrange
        val pokemonDetails = PokemonDetails(
            sprites = PokemonDetails.Sprites(
                backDefault = "definitionem",
                backShiny = "vocibus",
                frontDefault = "torquent",
                frontShiny = "ante"
            ), stats = listOf(
                PokemonDetails.Stats(
                    baseStat = 9840,
                    effort = 9832,
                    stat = PokemonDetails.Stat(
                        name = "Steve Myers",
                        url = "https://www.google.com/#q=prodesset"
                    )
                )
            ), height = 5320,
            weight = 9011

        )
        val expectedUiModel = PokemonDetailsContentUiModel(
            imageUrls = listOf(
                "definitionem",
                "vocibus",
                "torquent",
                "ante"
            ),
            stats = listOf(
                StatsUiModel(
                    baseStat = 9840,
                    effort = 9832,
                    name = "STEVE MYERS"
                ),
            ),
            height = 5320,
            weight = 9011
        )
        // Act
        val result = mapper.map(pokemonDetails)
        // Assert
        assertEquals(expectedUiModel, result)
    }
    @Test
    fun `map with 0 effort should return PokemonDetailsContentUiModel`() {
        // Arrange
        val pokemonDetails = PokemonDetails(
            sprites = PokemonDetails.Sprites(
                backDefault = "definitionem",
                backShiny = "vocibus",
                frontDefault = "torquent",
                frontShiny = "ante"
            ), stats = listOf(
                PokemonDetails.Stats(
                    baseStat = 9840,
                    effort = 0,
                    stat = PokemonDetails.Stat(
                        name = "Steve Myers",
                        url = "https://www.google.com/#q=prodesset"
                    )
                )
            ), height = 5320,
            weight = 9011

        )
        val expectedUiModel = PokemonDetailsContentUiModel(
            imageUrls = listOf(
                "definitionem",
                "vocibus",
                "torquent",
                "ante"
            ),
            stats = listOf(
                StatsUiModel(
                    baseStat = 9840,
                    effort = null,
                    name = "STEVE MYERS"
                ),
            ),
            height = 5320,
            weight = 9011
        )
        // Act
        val result = mapper.map(pokemonDetails)
        // Assert
        assertEquals(expectedUiModel, result)
    }
}

