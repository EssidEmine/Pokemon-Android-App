package com.bforbank.pokemon.ui.screens.pokemons.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PokemonsUiModel(
    val isLoading: Boolean = false,
    val error: PokemonsErrorUiModel? = null,
    val pokemons: List<PokemonUiModel>? = null,
    val isSearching: Boolean = false,
    val searchedPokemon: String= "",
    val navigation: PokemonsNavigation = PokemonsNavigation.NONE
) : Parcelable

@Parcelize
data class PokemonUiModel(
    val name: String,
    val id: Int,
) : Parcelable

@Parcelize
data class PokemonsErrorUiModel(
    val label: String,
    val type: PokemonsErrorUiModelType
) : Parcelable

enum class PokemonsErrorUiModelType {
    NETWORK,
    UNKNOWN
}

@Parcelize
sealed interface PokemonsNavigation : Parcelable {
    @Parcelize
    data object NONE : PokemonsNavigation

    @Parcelize
    data class ShowDetails(val id: Int) : PokemonsNavigation
}
