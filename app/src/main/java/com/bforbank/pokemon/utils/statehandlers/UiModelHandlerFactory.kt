package com.bforbank.pokemon.utils.statehandlers

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import com.bforbank.pokemon.ui.dispatchers.DispatcherProvider

class UiModelHandlerFactory(
    private val dispatcherProvider: DispatcherProvider,
) {

    fun <T : Parcelable> buildSavedStateUiStateHandler(
        savedStateHandle: SavedStateHandle,
        defaultUiModel: T,
    ): UiModelHandler<T> = UiModelSavedStateHandler(
        dispatcherProvider = dispatcherProvider,
        savedStateHandle = savedStateHandle,
        defaultUiModel = defaultUiModel,
    )

    fun <T : Parcelable> buildStandardUiStateHandler(
        defaultUiModel: T,
    ): UiModelHandler<T> = UiModelStandardHandler(
        defaultUiModel = defaultUiModel,
    )
}
