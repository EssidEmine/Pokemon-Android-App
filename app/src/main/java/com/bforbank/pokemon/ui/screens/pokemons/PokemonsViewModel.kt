package com.bforbank.pokemon.ui.screens.pokemons

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bforbank.pokemon.domain.models.Pokemons
import com.bforbank.pokemon.domain.models.PokemonsError
import com.bforbank.pokemon.domain.usecases.GetPokemonsUseCaseImpl
import com.bforbank.pokemon.ui.dispatchers.DispatcherProvider
import com.bforbank.pokemon.ui.screens.pokemons.mapper.PokemonsUiModelMapper
import com.bforbank.pokemon.ui.screens.pokemons.model.PokemonsErrorUiModel
import com.bforbank.pokemon.ui.screens.pokemons.model.PokemonsErrorUiModelType
import com.bforbank.pokemon.ui.screens.pokemons.model.PokemonsNavigation
import com.bforbank.pokemon.ui.screens.pokemons.model.PokemonsUiModel
import com.bforbank.pokemon.utils.Result
import com.bforbank.pokemon.utils.filterByText
import com.bforbank.pokemon.utils.statehandlers.UiModelHandlerFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonsViewModel @Inject constructor(
    private val getPokemonsUseCase: GetPokemonsUseCaseImpl,
    private val dispatcherProvider: DispatcherProvider,
    private val pokemonsUiModelMapper: PokemonsUiModelMapper,
    uiModelHandlerFactory: UiModelHandlerFactory,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val uiModelHandler = uiModelHandlerFactory.buildSavedStateUiStateHandler(
        savedStateHandle = savedStateHandle,
        defaultUiModel = PokemonsUiModel(),
    )
    val uiModelFlow get() = uiModelHandler.uiModelFlow
    var pokemons = Pokemons(emptyList())
    private var isSearching = false
    private var currentPagingOffset = 0


    init {
        viewModelScope.launch(dispatcherProvider.io) {

            uiModelHandler.updateUiModel { uiModel ->
                uiModel.copy(
                    isLoading = true
                )
            }
            getPokemonsUseCase(
                limit = POKEMONS_LIMIT,
                offset = currentPagingOffset
            )
                .collect {
                    when (val result = it) {
                        is Result.Error<PokemonsError> -> {
                            handleErrorType(result.error)
                        }

                        is Result.Success -> {
                            pokemons = result.data
                            uiModelHandler.updateUiModel { uiModel ->
                                uiModel.copy(
                                    pokemons = pokemonsUiModelMapper.map(pokemons),
                                    isLoading = false,
                                )
                            }
                        }
                    }
                }
        }
    }

    fun loadMore() {
        if (!isSearching) {
            viewModelScope.launch(dispatcherProvider.io) {
                val newOffset = currentPagingOffset + POKEMONS_LIMIT
                getPokemonsUseCase(
                    limit = POKEMONS_LIMIT,
                    offset = newOffset
                )
                    .collect {
                        when (val result = it) {
                            is Result.Error<PokemonsError> -> {} //not needed

                            is Result.Success -> {
                                val existingPokemonsContent = pokemons.content ?: emptyList()
                                val newPokemonsContent = result.data.content ?: emptyList()

                                pokemons = pokemons.copy(
                                    content = existingPokemonsContent + newPokemonsContent
                                )

                                val currentUiModel = uiModelHandler.uiModelFlow.value
                                val currentPokemonsUiModel = currentUiModel.pokemons ?: emptyList()
                                val newPokemonsUiModel = pokemonsUiModelMapper.map(result.data)

                                val updatedPokemonsUiModel = (
                                        currentPokemonsUiModel + newPokemonsUiModel
                                        ).sortedByDescending { it.name }.reversed().distinct()

                                uiModelHandler.updateUiModel { uiModel ->
                                    uiModel.copy(
                                        pokemons = updatedPokemonsUiModel,
                                    )
                                }

                                currentPagingOffset = newOffset
                            }
                        }
                    }
            }
        }
    }


    private fun handleErrorType(error: PokemonsError) {
        viewModelScope.launch(dispatcherProvider.io) {
            uiModelHandler.updateUiModel { uiModel ->
                uiModel.copy(
                    isLoading = false,
                    error = when (error) {
                        is PokemonsError.Network -> PokemonsErrorUiModel(
                            label = error.error,
                            type = PokemonsErrorUiModelType.NETWORK
                        )

                        is PokemonsError.Unknown -> PokemonsErrorUiModel(
                            label = error.error,
                            type = PokemonsErrorUiModelType.UNKNOWN
                        )
                    }
                )
            }
        }
    }

    fun onSearchTextChange(text: String) {
        isSearching = text.isNotEmpty()
        viewModelScope.launch(dispatcherProvider.io) {
            val filteredPokemons = pokemons.content?.filterByText(text) { pokemon ->
                pokemon.name
            } ?: pokemons.content

            filteredPokemons?.let {
                uiModelHandler.updateUiModel { uiModel ->
                    uiModel.copy(
                        pokemons = pokemonsUiModelMapper.map(
                            Pokemons(filteredPokemons)
                        ),
                        searchedPokemon = text
                    )
                }
            }
        }
    }

    fun showPokemonDetails(id: Int) {
        viewModelScope.launch(dispatcherProvider.io) {
            uiModelHandler.updateUiModel { uiModel ->
                uiModel.copy(
                    navigation = PokemonsNavigation.ShowDetails(id)
                )
            }
        }
    }

    fun resetNavigation() {
        viewModelScope.launch(dispatcherProvider.io) {
            uiModelHandler.updateUiModel { uiModel ->
                uiModel.copy(
                    navigation = PokemonsNavigation.NONE
                )
            }
        }
    }

    companion object {
        const val POKEMONS_LIMIT = 20
    }
}
