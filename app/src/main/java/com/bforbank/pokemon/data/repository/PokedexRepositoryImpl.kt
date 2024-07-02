package com.bforbank.pokemon.data.repository

import com.bforbank.pokemon.data.mapper.PokemonDetailsMapper
import com.bforbank.pokemon.data.mapper.PokemonsMapper
import com.bforbank.pokemon.data.network.ApiService
import com.bforbank.pokemon.domain.models.PokemonDetails
import com.bforbank.pokemon.domain.models.PokemonDetailsError
import com.bforbank.pokemon.domain.models.Pokemons
import com.bforbank.pokemon.domain.models.PokemonsError
import com.bforbank.pokemon.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PokedexRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val pokemonDetails: PokemonDetailsMapper,
    private val pokemonsMapper: PokemonsMapper,
) : PokedexRepository {

    override suspend fun getPokemons(
        limit: Int,
        offset: Int
    ): Flow<Result<Pokemons, PokemonsError>> {
        return try {
            val pokemons = apiService.getAllPokemon(
                limit = limit,
                offset = offset,
            )
            flow { emit(pokemonsMapper.map(pokemons)) }
        } catch (e: Exception) {
            flow { emit(Result.Error(PokemonsError.Unknown(e.message.toString()))) }
        }
    }

    override suspend fun getPokemonDetails(id: Int): Flow<Result<PokemonDetails, PokemonDetailsError>> {

        return try {
            val detailedPokemon = apiService.getPokemonDetails(id)
            flow { emit(pokemonDetails.map(detailedPokemon)) }
        } catch (e: Exception) {
            flow { emit(Result.Error(PokemonDetailsError.Generic(e.message.toString()))) }
        }
    }
}


