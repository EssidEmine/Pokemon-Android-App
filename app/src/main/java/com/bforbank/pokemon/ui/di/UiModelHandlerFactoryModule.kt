package com.bforbank.pokemon.ui.di

import com.bforbank.pokemon.ui.dispatchers.DispatcherProvider
import com.bforbank.pokemon.utils.statehandlers.UiModelHandlerFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class UiModelHandlerFactoryModule {

    @Provides
    fun provideUiModelHandlerFactory(dispatcherProvider: DispatcherProvider): UiModelHandlerFactory {
        return UiModelHandlerFactory(dispatcherProvider)
    }
}
