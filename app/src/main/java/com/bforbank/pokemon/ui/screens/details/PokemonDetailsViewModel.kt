package com.bforbank.pokemon.ui.screens.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bforbank.pokemon.domain.models.PokemonDetailsError
import com.bforbank.pokemon.domain.usecases.GetPokemonDetailsUseCaseImpl
import com.bforbank.pokemon.ui.Destination.Companion.POKEMON_DETAILS_ARGS
import com.bforbank.pokemon.ui.dispatchers.DispatcherProvider
import com.bforbank.pokemon.ui.screens.details.mapper.PokemonDetailsUiModelMapper
import com.bforbank.pokemon.ui.screens.details.model.PokemonDetailsErrorUiModel
import com.bforbank.pokemon.ui.screens.details.model.PokemonDetailsUiModel
import com.bforbank.pokemon.ui.screens.details.model.PokemonDetailsUiModelType
import com.bforbank.pokemon.utils.Result
import com.bforbank.pokemon.utils.statehandlers.UiModelHandlerFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailsViewModel @Inject constructor(
    private val getPokemonDetailsUseCaseImpl: GetPokemonDetailsUseCaseImpl,
    private val pokemonDetailsUiModelMapper: PokemonDetailsUiModelMapper,
    val dispatcherProvider: DispatcherProvider,
    uiModelHandlerFactory: UiModelHandlerFactory,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val uiModelHandler = uiModelHandlerFactory.buildSavedStateUiStateHandler(
        savedStateHandle = savedStateHandle,
        defaultUiModel = PokemonDetailsUiModel(),
    )
    private val pokemonId: Int? = savedStateHandle.get<Int>(POKEMON_DETAILS_ARGS)
    val uiModelFlow get() = uiModelHandler.uiModelFlow

    init {
        viewModelScope.launch(dispatcherProvider.io) {
            if (pokemonId != null) {
                uiModelHandler.updateUiModel { uiModel ->
                    uiModel.copy(
                        isLoading = true
                    )
                }
                getPokemonDetailsUseCaseImpl.invoke(pokemonId)
                    .collect {
                        when (val result = it) {
                            is Result.Error<PokemonDetailsError> -> {
                                handleErrorType(result.error)
                            }

                            is Result.Success -> {
                                uiModelHandler.updateUiModel { uiModel ->
                                    uiModel.copy(
                                        isLoading = false,
                                        content = pokemonDetailsUiModelMapper.map(result.data)
                                    )
                                }
                            }
                        }
                    }
            } else {
                uiModelHandler.updateUiModel { uiModel ->
                    uiModel.copy(
                        isLoading = false,
                        error = PokemonDetailsErrorUiModel(
                            label = "Pokemon Name is null error",
                            type = PokemonDetailsUiModelType.GENERIC
                        )
                    )
                }
            }
        }
    }

    private fun handleErrorType(error: PokemonDetailsError) {
        viewModelScope.launch(dispatcherProvider.io) {
            uiModelHandler.updateUiModel { uiModel ->
                uiModel.copy(
                    isLoading = false,
                    error = when (error) {
                        is PokemonDetailsError.Network -> PokemonDetailsErrorUiModel(
                            label = error.error,
                            type = PokemonDetailsUiModelType.NETWORK
                        )

                        is PokemonDetailsError.Generic -> PokemonDetailsErrorUiModel(
                            label = error.error,
                            type = PokemonDetailsUiModelType.GENERIC
                        )
                    }
                )
            }
        }
    }
}
