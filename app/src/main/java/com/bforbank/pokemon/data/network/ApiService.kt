package com.bforbank.pokemon.data.network

import com.bforbank.pokemon.data.model.PokemonDetailsDto
import com.bforbank.pokemon.data.model.PokemonsDto
import retrofit2.Response
import javax.inject.Inject

class ApiService @Inject constructor(
    private val endpointApi: EndpointApi
) {

    suspend fun getPokemonDetails(id: Int): Response<PokemonDetailsDto> {
        return endpointApi.getSinglePokemon(id)
    }

    suspend fun getAllPokemon(
        limit: Int,
        offset: Int
    ): Response<PokemonsDto> {
        return endpointApi.getPokemons(
            limit = limit,
            offset = offset,
        )
    }
}
