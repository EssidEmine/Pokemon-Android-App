package com.bforbank.pokemon.ui.di

import com.bforbank.pokemon.ui.dispatchers.DispatcherProvider
import com.bforbank.pokemon.ui.dispatchers.DispatcherProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
class CoroutineDispatcherModule {

    @Provides
    fun providesDispatcherProvider(): DispatcherProvider {
        return DispatcherProviderImpl(
            main = Dispatchers.Main,
            io = Dispatchers.IO,
        )
    }
}
