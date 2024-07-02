package com.bforbank.pokemon.domain.usecases

import com.bforbank.pokemon.data.repository.PokedexRepository
import com.bforbank.pokemon.domain.models.PokemonDetails
import com.bforbank.pokemon.domain.models.PokemonDetailsError
import com.bforbank.pokemon.utils.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPokemonDetailsUseCaseImpl @Inject constructor(
    private val pokedexRepository: PokedexRepository
) {

    suspend fun invoke(id: Int): Flow<Result<PokemonDetails, PokemonDetailsError>> {
        return pokedexRepository.getPokemonDetails(id)
    }
}
