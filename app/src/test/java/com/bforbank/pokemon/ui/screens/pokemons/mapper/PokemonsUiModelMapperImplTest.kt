package com.bforbank.pokemon.ui.screens.pokemons.mapper

import com.bforbank.pokemon.domain.models.Pokemon
import com.bforbank.pokemon.domain.models.Pokemons
import com.bforbank.pokemon.ui.screens.pokemons.model.PokemonUiModel
import org.junit.Assert.assertEquals
import org.junit.Test

class PokemonsUiModelMapperImplTest {

    private val mapper = PokemonsUiModelMapperImpl()

    @Test
    fun `map should return list of PokemonUiModel`() {
        // Arrange
        val pokemons = Pokemons(
            content = listOf(
                Pokemon(
                    id = 1,
                    name = "pikachu",
                ),
                Pokemon(
                    id = 2,
                    name = "charmander",
                )
            )
        )
        val expectedUiModels = listOf(
            PokemonUiModel(
                name = "Charmander",
                id = 2,
            ),
            PokemonUiModel(
                name = "Pikachu",
                id = 1,
            ),
        )
        // Act
        val result = mapper.map(pokemons)
        // Assert
        assertEquals(expectedUiModels, result)
    }

    @Test
    fun `map should return empty list when pokemons content is null`() {
        // Arrange
        val expectedUiModels = emptyList<PokemonUiModel>()
        // Act
        val result = mapper.map(Pokemons(content = null))
        // Assert
        assertEquals(expectedUiModels, result)
    }

    @Test
    fun `map should return empty list when pokemons content is empty`() {
        // Arrange
        val expectedUiModels = emptyList<PokemonUiModel>()
        // Act
        val result = mapper.map(Pokemons(content = emptyList()))
        // Assert
        assertEquals(expectedUiModels, result)
    }
}
