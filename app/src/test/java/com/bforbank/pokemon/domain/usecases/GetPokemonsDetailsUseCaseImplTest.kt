package com.bforbank.pokemon.domain.usecases

import com.bforbank.pokemon.data.repository.PokedexRepository
import com.bforbank.pokemon.domain.models.PokemonDetails
import com.bforbank.pokemon.domain.models.PokemonDetailsError
import com.bforbank.pokemon.utils.Result
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any

@RunWith(MockitoJUnitRunner::class)
class GetPokemonsDetailsUseCaseImplTest {

    @Mock
    private lateinit var pokedexRepository: PokedexRepository
    private lateinit var getPokemonDetailsUseCase: GetPokemonDetailsUseCaseImpl

    @Before
    fun setUp() {
        getPokemonDetailsUseCase = GetPokemonDetailsUseCaseImpl(pokedexRepository)
    }

    @Test
    fun `invoke should return PokemonDetails when result is success`() = runTest {
        // Arrange
        val pokemonDetails = PokemonDetails(
            sprites = PokemonDetails.Sprites(
                backDefault = "backDefaultUrl",
                backShiny = "backShinyUrl",
                frontDefault = "frontDefaultUrl",
                frontShiny = "frontShinyUrl"
            ),
            stats = listOf(
                PokemonDetails.Stats(
                    baseStat = 45,
                    effort = 1,
                    stat = PokemonDetails.Stat(
                        name = "speed",
                        url = "speedUrl"
                    )
                )
            ),
            height = 10,
            weight = 100
        )
        val expectedResult = Result.Success(pokemonDetails)

        given(pokedexRepository.getPokemonDetails(any())).willReturn(
            flow {
                emit(Result.Success(pokemonDetails))
            }
        )
        // Act
        val result = getPokemonDetailsUseCase.invoke(1).first()
        // Assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun `invoke should return error when result is Generic error`() = runTest {
        // Arrange
        val givenId = 1
        val expectedResult = Result.Error(PokemonDetailsError.Generic("Unknown"))

        given(pokedexRepository.getPokemonDetails(givenId)).willReturn(flow {
            emit(Result.Error(PokemonDetailsError.Generic("Unknown")))
        })
        // Act
        val result = getPokemonDetailsUseCase.invoke(givenId).first()
        // Assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun `invoke should return error when result is Network error`() = runTest {
        // Arrange
        val givenId = 1
        val expectedResult = Result.Error(PokemonDetailsError.Network("Network"))

        given(pokedexRepository.getPokemonDetails(givenId)).willReturn(flow {
            emit(Result.Error(PokemonDetailsError.Network("Network")))
        })
        // Act
        val result = getPokemonDetailsUseCase.invoke(givenId).first()
        // Assert
        assertEquals(expectedResult, result)
    }
}
