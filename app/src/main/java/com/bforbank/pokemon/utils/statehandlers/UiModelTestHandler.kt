package com.bforbank.pokemon.utils.statehandlers

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UiModelTestHandler<T>(val mutableStateFlow: MutableStateFlow<T>) :
    UiModelHandler<T> {

    override val uiModelFlow: StateFlow<T>
        get() = mutableStateFlow.asStateFlow()
    val lastValue: T? get() = mutableListOfValues.getOrNull(mutableListOfValues.lastIndex)

    private var mutableListOfValues = MutableListOfValues<T>()
    val listOfValues: List<T> get() = mutableListOfValues

    override suspend fun updateUiModel(getNewValue: (currentValue: T) -> T) {
        val newValue = getNewValue(uiModelFlow.value)
        mutableListOfValues.add(newValue)
        mutableStateFlow.value = newValue
    }

    fun expectNoMoreValue() = assert(!mutableListOfValues.hasBiggerValue())

    private class MutableListOfValues<T> : ArrayList<T>() {

        private var biggestValueRetrievedIndex: Int = -1

        override fun get(index: Int): T {
            if (index > biggestValueRetrievedIndex) biggestValueRetrievedIndex = index

            return super.get(index)
        }

        fun hasBiggerValue(): Boolean = getOrNull(biggestValueRetrievedIndex + 1) != null
    }
}
