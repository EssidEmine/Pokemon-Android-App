package com.bforbank.pokemon.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class PokemonDetailsDto(
    val sprites: Sprites,
    val stats: List<Stats>,
    val height: Int,
    val weight: Int
) {
    @Keep
    data class Sprites(
        @SerializedName("back_default")
        val backDefault: String,
        @SerializedName("back_shiny")
        val backShiny: String,
        @SerializedName("front_default")
        val frontDefault: String,
        @SerializedName("front_shiny")
        val frontShiny: String
    )

    @Keep
    data class Stats(
        @SerializedName("base_stat")
        val baseStat: Int,
        val effort: Int,
        val stat: Stat
    )

    @Keep
    data class Stat(
        val name: String,
        val url: String
    )
}

