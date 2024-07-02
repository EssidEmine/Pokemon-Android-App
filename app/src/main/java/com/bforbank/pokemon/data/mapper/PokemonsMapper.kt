package com.bforbank.pokemon.data.mapper

import com.bforbank.pokemon.data.model.PokemonsDto
import com.bforbank.pokemon.domain.models.Pokemons
import com.bforbank.pokemon.domain.models.PokemonsError
import com.bforbank.pokemon.utils.Result
import retrofit2.Response

interface PokemonsMapper {

    fun map(response: Response<PokemonsDto>): Result<Pokemons, PokemonsError>
}
