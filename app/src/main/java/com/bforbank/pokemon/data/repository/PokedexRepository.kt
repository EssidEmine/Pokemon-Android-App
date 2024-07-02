package com.bforbank.pokemon.data.repository

import com.bforbank.pokemon.domain.models.PokemonDetails
import com.bforbank.pokemon.domain.models.PokemonDetailsError
import com.bforbank.pokemon.domain.models.Pokemons
import com.bforbank.pokemon.domain.models.PokemonsError
import com.bforbank.pokemon.utils.Result
import kotlinx.coroutines.flow.Flow

interface PokedexRepository {

    suspend fun getPokemons(
        limit: Int,
        offset: Int
    ): Flow<Result<Pokemons, PokemonsError>>

    suspend fun getPokemonDetails(id: Int): Flow<Result<PokemonDetails, PokemonDetailsError>>
}
