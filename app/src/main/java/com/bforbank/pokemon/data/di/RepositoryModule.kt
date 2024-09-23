package com.bforbank.pokemon.data.di

import com.bforbank.pokemon.data.mapper.PokemonDetailsMapper
import com.bforbank.pokemon.data.mapper.PokemonsMapper
import com.bforbank.pokemon.data.network.ApiService
import com.bforbank.pokemon.data.repository.PokedexMockedRepositoryImpl
import com.bforbank.pokemon.data.repository.PokedexRepository
import com.bforbank.pokemon.data.repository.PokedexRepositoryImpl
import com.test.bfor.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    /*
      @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
    }
     */
    @Provides
    @Production
    fun bindProductionPokedexRepository(
        apiService: ApiService,
        pokemonDetails: PokemonDetailsMapper,
        pokemonsMapper: PokemonsMapper,
    ): PokedexRepository {
        return PokedexRepositoryImpl(apiService, pokemonDetails, pokemonsMapper)
    }

    @Provides
    @Mock
    fun bindPokedexMockedRepository(): PokedexRepository {
        return PokedexMockedRepositoryImpl()
    }

    @Provides
    @Singleton
    fun providePokedexRepository(
        @Production productionService: PokedexRepository,
        @Mock mockService: PokedexRepository,
    ): PokedexRepository {
        //val isMockActivated = sharedPreferences.getBoolean("mockSomthing", false)
        return if (BuildConfig.FLAVOR != "mock") {
            productionService
        } else {
            mockService
        }
    }
}