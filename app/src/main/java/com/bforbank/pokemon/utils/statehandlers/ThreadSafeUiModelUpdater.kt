package com.bforbank.pokemon.utils.statehandlers

import com.bforbank.pokemon.ui.dispatchers.DispatcherProvider
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ThreadSafeUiModelUpdater @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
) {

    private val mutex = Mutex()

    suspend fun <T> updateUiModel(
        flow: StateFlow<T>,
        getNewValue: (currentValue: T) -> T,
        updateValue: (T) -> Unit
    ) {
        withContext(dispatcherProvider.io) {
            mutex.withLock {
                val newValue = getNewValue(flow.value)
                withContext(dispatcherProvider.main) {
                    updateValue(newValue)
                }
            }
        }
    }
}
