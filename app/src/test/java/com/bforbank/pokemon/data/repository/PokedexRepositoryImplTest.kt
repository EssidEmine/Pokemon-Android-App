package com.bforbank.pokemon.data.repository

import com.bforbank.pokemon.data.mapper.PokemonDetailsMapper
import com.bforbank.pokemon.data.mapper.PokemonsMapper
import com.bforbank.pokemon.data.model.PokemonDetailsDto
import com.bforbank.pokemon.data.model.PokemonsDto
import com.bforbank.pokemon.data.network.ApiService
import com.bforbank.pokemon.domain.models.Pokemon
import com.bforbank.pokemon.domain.models.PokemonDetails
import com.bforbank.pokemon.domain.models.PokemonDetailsError
import com.bforbank.pokemon.domain.models.Pokemons
import com.bforbank.pokemon.domain.models.PokemonsError
import com.bforbank.pokemon.utils.Result
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class PokedexRepositoryImplTest {

    @Mock
    private lateinit var apiService: ApiService

    @Mock
    private lateinit var pokemonDetailsMapper: PokemonDetailsMapper

    @Mock
    private lateinit var pokemonsMapper: PokemonsMapper

    private lateinit var repository: PokedexRepositoryImpl

    @Before
    fun setUp() {
        repository = PokedexRepositoryImpl(apiService, pokemonDetailsMapper, pokemonsMapper)
    }

    @Test
    fun `getPokemons Success should return Result Success of Pokemons`() = runTest {
        // Arrange
        val pokemonsDto = PokemonsDto(
            count = 10,
            next = "next",
            previous = "previous",
            results = listOf(
                PokemonsDto.PokemonResult(
                    name = "Carlos Kemp",
                    url = "https://duckduckgo.com/?q=verear/1"

                ),
                PokemonsDto.PokemonResult(
                    name = "Pika",
                    url = "https://duckduckgo.com/?q=pika/2"

                )
            )
        )
        val response = Response.success(pokemonsDto)
        val expectedPokemons = Pokemons(
            content = listOf(
                Pokemon(
                    name = "Carlos Kemp",
                    id = 1,
                ),
                Pokemon(
                    name = "Pika",
                    id = 2,
                ),
            )
        )
        val expectedResult = Result.Success(expectedPokemons)

        given(apiService.getAllPokemon(any(), any())).willReturn(response)
        given(pokemonsMapper.map(response)).willReturn(expectedResult)

        // Act
        val result = repository.getPokemons(20, 0).first()

        // Assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun `getPokemons Error should return Result Error from api service`() = runTest {
        // Arrange
        val errorMessage = "Not Found"
        val expectedResult = Result.Error(PokemonsError.Unknown("Not Found"))

        given(apiService.getAllPokemon(any(), any())).willThrow(RuntimeException(errorMessage))

        // Act
        val result = repository.getPokemons(20, 0).first()

        // Assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun `getPokemons Error should return Result Error from mapper`() = runTest {
        // Arrange
        val errorMessage = "Not Found"
        val response = Response.error<PokemonsDto>(
            404,
            errorMessage.toResponseBody(null)
        )
        val expectedResult = Result.Error(PokemonsError.Unknown(response.message()))

        given(apiService.getAllPokemon(any(), any())).willReturn(response)
        given(pokemonsMapper.map(response)).willReturn(expectedResult)

        // Act
        val result = repository.getPokemons(20, 0).first()

        // Assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun `getPokemonDetails Success should return Result Success of PokemonDetails`() = runTest {
        // Arrange
        val pokemonDetailsDto = PokemonDetailsDto(
            sprites = PokemonDetailsDto.Sprites(
                backDefault = "back_default_url",
                backShiny = "back_shiny_url",
                frontDefault = "front_default_url",
                frontShiny = "front_shiny_url"
            ),
            stats = listOf(
                PokemonDetailsDto.Stats(
                    baseStat = 100,
                    effort = 50,
                    stat = PokemonDetailsDto.Stat(
                        name = "speed",
                        url = "speed_stat_url"
                    )
                )
            ),
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

        given(apiService.getPokemonDetails(any())).willReturn(response)
        given(pokemonDetailsMapper.map(response)).willReturn(expectedResult)

        // Act
        val result = repository.getPokemonDetails(1).first()

        // Assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun `getPokemonDetails Error should return Result Error from api service`() = runTest {
        // Arrange
        val errorMessage = "Not Found"
        val expectedResult = Result.Error(PokemonDetailsError.Generic("Not Found"))

        given(apiService.getPokemonDetails(any())).willThrow(RuntimeException(errorMessage))

        // Act
        val result = repository.getPokemonDetails(1).first()

        // Assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun `getPokemonDetails Error should return Result Error from mapper`() = runTest {
        // Arrange
        val errorMessage = "Not Found"
        val response = Response.error<PokemonDetailsDto>(
            404,
            errorMessage.toResponseBody(null)
        )
        val expectedResult = Result.Error(PokemonDetailsError.Generic(response.message()))

        given(apiService.getPokemonDetails(any())).willReturn(response)
        given(pokemonDetailsMapper.map(response)).willReturn(expectedResult)

        // Act
        val result = repository.getPokemonDetails(1).first()

        // Assert
        assertEquals(expectedResult, result)
    }
}
