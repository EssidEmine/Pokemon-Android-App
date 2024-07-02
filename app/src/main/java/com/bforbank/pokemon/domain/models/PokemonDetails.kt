package com.bforbank.pokemon.domain.models


data class PokemonDetails(
    val sprites: Sprites,
    val stats: List<Stats>,
    val height: Int,
    val weight: Int
) {
    data class Sprites(
        val backDefault: String,
        val backShiny: String,
        val frontDefault: String,
        val frontShiny: String
    )

    data class Stats(
        val baseStat: Int,
        val effort: Int,
        val stat: Stat
    )

    data class Stat(
        val name: String,
        val url: String
    )
}


sealed class PokemonDetailsError {
    data class Generic(
        val error: String,
    ) : PokemonDetailsError()

    data class Network(
        val error: String,
    ) : PokemonDetailsError()
}
