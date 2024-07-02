package com.bforbank.pokemon.domain.usecases

import com.bforbank.pokemon.data.repository.PokedexRepository
import javax.inject.Inject

class GetPokemonsUseCaseImpl @Inject constructor(
    private val pokedexRepository: PokedexRepository
) {
    suspend operator fun invoke(
        limit: Int,
        offset: Int
    ) = pokedexRepository.getPokemons(
        limit = limit,
        offset = offset
    )
}
