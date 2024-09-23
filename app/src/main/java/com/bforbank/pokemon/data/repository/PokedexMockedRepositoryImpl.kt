package com.bforbank.pokemon.data.repository

import com.bforbank.pokemon.domain.models.Pokemon
import com.bforbank.pokemon.domain.models.PokemonDetails
import com.bforbank.pokemon.domain.models.PokemonDetailsError
import com.bforbank.pokemon.domain.models.Pokemons
import com.bforbank.pokemon.domain.models.PokemonsError
import com.bforbank.pokemon.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PokedexMockedRepositoryImpl @Inject constructor() : PokedexRepository {

    override suspend fun getPokemons(
        limit: Int, offset: Int
    ): Flow<Result<Pokemons, PokemonsError>> = flow {
        emit(
            Result.Success(
                Pokemons(
                    content = listOf(
                        Pokemon(
                            name = "Mock Pika",
                            id = 1,
                        ),
                        Pokemon(
                            name = "Mock Bulbi",
                            id = 2,
                        ),
                    )
                )
            )
        )
    }

    override suspend fun getPokemonDetails(id: Int): Flow<Result<PokemonDetails, PokemonDetailsError>> = flow {
        emit(Result.Error(PokemonDetailsError.Generic("stop using mocks to show details, you dumb !")))
    }
}




