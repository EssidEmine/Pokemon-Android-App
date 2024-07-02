package com.bforbank.pokemon.utils.statehandlers

import android.os.Parcelable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class UiModelStandardHandler<T : Parcelable> @Inject constructor(
    defaultUiModel: T,
) : UiModelHandler<T> {

    private val uiModelMutableFlow = MutableStateFlow(defaultUiModel)
    override val uiModelFlow = uiModelMutableFlow.asStateFlow()

    override suspend fun updateUiModel(getNewValue: (currentValue: T) -> T) {
        uiModelMutableFlow.update { getNewValue(it) }
    }
}
