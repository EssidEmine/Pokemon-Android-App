package com.bforbank.pokemon.data.mapper

import com.bforbank.pokemon.data.model.PokemonDetailsDto
import com.bforbank.pokemon.domain.models.PokemonDetails
import com.bforbank.pokemon.domain.models.PokemonDetailsError
import com.bforbank.pokemon.utils.Result
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class PokemonDetailsMapperImplTest {

    private val mapper = PokemonDetailsMapperImpl()

    @Test
    fun `map when Response IsSuccessful should Return Mapped PokemonDetails`() {
        // Arrange
        val sprites = PokemonDetailsDto.Sprites(
            "back_default_url",
            "back_shiny_url",
            "front_default_url",
            "front_shiny_url"
        )
        val stats = listOf(
            PokemonDetailsDto.Stats(
                baseStat = 100,
                effort = 50,
                stat = PokemonDetailsDto.Stat(
                    name = "speed",
                    url = "speed_stat_url"
                )
            )
        )
        val pokemonDetailsDto = PokemonDetailsDto(
            sprites = sprites,
            stats = stats,
            height = 180,
            weight = 75
        )
        val response = Response.success(pokemonDetailsDto)

        val expectedPokemonDetails = PokemonDetails(
            sprites = PokemonDetails.Sprites(
                backDefault = "back_default_url",
                backShiny = "back_shiny_url",
                frontDefault = "front_default_url",
                frontShiny = "front_shiny_url"
            ),
            stats = listOf(
                PokemonDetails.Stats(
                    baseStat = 100,
                    effort = 50,
                    stat = PokemonDetails.Stat(
                        name = "speed",
                        url = "speed_stat_url"
                    )
                )
            ),
            height = 180,
            weight = 75
        )
        val expectedResult = Result.Success(expectedPokemonDetails)

        // Act
        val result = mapper.map(response)

        // Assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun `map when Response IsError should Return Error Result`() {
        // Arrange
        val response = Response.error<PokemonDetailsDto>(
            404,
            "Not Found".toResponseBody(null)
        )

        // Act
        val result = mapper.map(response)

        // Assert
        assertTrue(result is Result.Error && result.error is PokemonDetailsError.Network)

    }
}
