package com.bforbank.pokemon.data.mapper

import com.bforbank.pokemon.data.model.PokemonsDto
import com.bforbank.pokemon.domain.models.Pokemon
import com.bforbank.pokemon.domain.models.Pokemons
import com.bforbank.pokemon.domain.models.PokemonsError
import com.bforbank.pokemon.utils.Result
import com.bforbank.pokemon.utils.Result.Success
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Response

class PokemonsImplMapperTest {

    private var mapper = PokemonsMapperImpl()

    @Test
    fun `map when Response IsSuccessful should Return Mapped Pokemons`() {
        // Arrange
        val pokemonResult = PokemonsDto.PokemonResult(
            name = "Pokemon Name",
            url = "https://pokeapi.co/api/v2/pokemon/123/"
        )
        val pokemonsDto = PokemonsDto(
            count = 1,
            next = null,
            previous = null,
            results = listOf(pokemonResult)
        )
        val response = Response.success(pokemonsDto)

        val expectedPokemons = Pokemons(
            listOf(
                Pokemon(
                    id = 123,
                    name = "Pokemon Name"
                )
            )
        )
        val expectedResult = Success(expectedPokemons)

        // Act & Assert
        assertEquals(expectedResult, mapper.map(response))
    }

    @Test
    fun `map when Response Is Error should Return Error Result`() {
        // Arrange
        val response = Response.error<PokemonsDto>(
            404,
            "Not Found".toResponseBody(null)
        )
        // Act
        val result = mapper.map(response)
        // Assert
        Assert.assertTrue(result is Result.Error && result.error is PokemonsError.Network)

    }
}
