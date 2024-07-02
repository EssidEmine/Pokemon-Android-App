package com.bforbank.pokemon.data.mapper

import com.bforbank.pokemon.data.model.PokemonDetailsDto
import com.bforbank.pokemon.domain.models.PokemonDetails
import com.bforbank.pokemon.domain.models.PokemonDetailsError
import com.bforbank.pokemon.utils.Result
import retrofit2.Response

interface PokemonDetailsMapper {

    fun map(response: Response<PokemonDetailsDto>): Result<PokemonDetails, PokemonDetailsError>
}
