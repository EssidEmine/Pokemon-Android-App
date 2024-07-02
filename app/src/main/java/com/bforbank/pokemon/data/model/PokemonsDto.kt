package com.bforbank.pokemon.data.model

import androidx.annotation.Keep

@Keep
data class PokemonsDto(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonResult>
) {
    @Keep
    data class PokemonResult(
        val name: String,
        val url: String
    )
}

