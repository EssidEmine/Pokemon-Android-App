package com.bforbank.pokemon.ui.screens.details.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PokemonDetailsUiModel(
    val isLoading: Boolean = false,
    val error: PokemonDetailsErrorUiModel? = null,
    val content: PokemonDetailsContentUiModel? = null,
) : Parcelable

@Parcelize
data class PokemonDetailsContentUiModel(
    val imageUrls: List<String>,
    val stats: List<StatsUiModel>,
    val height: Int,
    val weight: Int
) : Parcelable

@Parcelize
data class StatsUiModel(
    val baseStat: Int,
    val effort: Int,
    val name: String
) : Parcelable

@Parcelize
data class PokemonDetailsErrorUiModel(
    val label: String,
    val type: PokemonDetailsUiModelType
) : Parcelable

enum class PokemonDetailsUiModelType {
    NETWORK,
    GENERIC,
}
