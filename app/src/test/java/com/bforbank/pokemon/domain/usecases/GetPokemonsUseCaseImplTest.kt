package com.bforbank.pokemon.domain.usecases

import com.bforbank.pokemon.data.repository.PokedexRepository
import com.bforbank.pokemon.domain.models.Pokemons
import com.bforbank.pokemon.domain.models.PokemonsError
import com.bforbank.pokemon.utils.Result
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
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
class GetPokemonsUseCaseImplTest {

    @Mock
    private lateinit var pokedexRepository: PokedexRepository
    private lateinit var useCase: GetPokemonsUseCaseImpl

    @Before
    fun setUp() {
        useCase = GetPokemonsUseCaseImpl(pokedexRepository)
    }

    @Test
    fun `invoke should return pokemons when repository return success `() = runTest {
        // Arrange
        val expectedPokemons = Pokemons(emptyList())
        val expectedResult = Result.Success(expectedPokemons)

        given(pokedexRepository.getPokemons(limit = any(), offset = any())).willReturn(
            flowOf(
                expectedResult
            )
        )
        // Act
        val result = useCase.invoke(limit = 30, offset = 0).first()
        // Assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun `invoke should return error when repository return Unknown error`() = runTest {
        // Arrange
        val expectedResult = Result.Error(PokemonsError.Unknown("Unknown"))

        given(pokedexRepository.getPokemons(limit = any(), offset = any())).willReturn(
            flowOf(
                expectedResult
            )
        )
        // Act
        val result = useCase.invoke(limit = 30, offset = 0).first()
        // Assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun `invoke should return error when repository return Network error`() = runTest {
        // Arrange
        val expectedResult = Result.Error(PokemonsError.Network("Network"))

        given(pokedexRepository.getPokemons(any(), any())).willReturn(flowOf(expectedResult))
        // Act
        val result = useCase.invoke(limit = 30, offset = 0).first()
        // Assert
        assertEquals(expectedResult, result)
    }
}

