package com.bforbank.pokemon.ui.dispatchers

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {

    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
}
