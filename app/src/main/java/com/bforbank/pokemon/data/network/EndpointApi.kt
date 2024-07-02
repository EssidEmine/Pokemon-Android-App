package com.bforbank.pokemon.data.network

import com.bforbank.pokemon.data.model.PokemonDetailsDto
import com.bforbank.pokemon.data.model.PokemonsDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EndpointApi {
    @GET("pokemon/")
    suspend fun getPokemons(
        @Query("limit") limit: Int?,
        @Query("offset") offset: Int?
    ): Response<PokemonsDto>

    @GET("pokemon/{id}/")
    suspend fun getSinglePokemon(
        @Path("id") id: Int
    ): Response<PokemonDetailsDto>

}
