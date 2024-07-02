package com.bforbank.pokemon.utils.statehandlers

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import com.bforbank.pokemon.ui.dispatchers.DispatcherProvider

class UiModelSavedStateHandler<T : Parcelable>(
    private val savedStateHandle: SavedStateHandle,
    dispatcherProvider: DispatcherProvider,
    defaultUiModel: T,
) : UiModelHandler<T> {

    private val savedStateKey = defaultUiModel::class.java.simpleName
    private val uiModelUpdater = ThreadSafeUiModelUpdater(dispatcherProvider)
    override val uiModelFlow = savedStateHandle.getStateFlow(savedStateKey, defaultUiModel)

    override suspend fun updateUiModel(getNewValue: (currentValue: T) -> T) {
        uiModelUpdater.updateUiModel(uiModelFlow, getNewValue) { newValue ->
            savedStateHandle[savedStateKey] = newValue
        }
    }
}
