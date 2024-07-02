package com.bforbank.pokemon.ui

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Destination(
    val route: String
) {

    data object Pokemons : Destination("pokemon")
    data object PokemonsDetails : Destination("pokemonDetails/{$POKEMON_DETAILS_ARGS}") {

        fun buildUri(id: Int) = "pokemonDetails/$id"

        fun pokemonDetailsNavArgument() = navArgument(POKEMON_DETAILS_ARGS) {
            type = NavType.IntType
            nullable = false
        }
    }

    companion object {

        const val POKEMON_DETAILS_ARGS = "id"
    }
}
