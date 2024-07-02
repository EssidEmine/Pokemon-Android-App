package com.bforbank.pokemon.utils.statehandlers

import kotlinx.coroutines.flow.StateFlow

interface UiModelHandler<T> {

    val uiModelFlow: StateFlow<T>
    suspend fun updateUiModel(getNewValue: (currentValue: T) -> T)
}
