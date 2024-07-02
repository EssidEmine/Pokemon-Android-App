package com.bforbank.pokemon.domain.models

data class Pokemons(
    val content: List<Pokemon>? = null
)

data class Pokemon(
    val name: String,
    val id: Int,
)

sealed class PokemonsError {
    data class Unknown(
        val error: String,
    ) : PokemonsError()

    data class Network(
        val error: String,
    ) : PokemonsError()
}
