package com.bforbank.pokemon.data.mapper


import com.bforbank.pokemon.data.model.PokemonsDto
import com.bforbank.pokemon.domain.models.Pokemon
import com.bforbank.pokemon.domain.models.Pokemons
import com.bforbank.pokemon.domain.models.PokemonsError
import com.bforbank.pokemon.utils.Result
import retrofit2.Response
import javax.inject.Inject

class PokemonsMapperImpl @Inject constructor() : PokemonsMapper {

    override fun map(response: Response<PokemonsDto>): Result<Pokemons, PokemonsError> {
        return if (response.isSuccessful) {
            Result.Success(
                Pokemons(
                    content = response.body()?.results?.map { pokemon ->
                        Pokemon(
                            name = pokemon.name,
                            id = extractLastDigitFromUrl(pokemon.url),
                        )
                    }

                )
            )
        } else {
            Result.Error(PokemonsError.Network(response.message()))
        }
    }
}

fun extractLastDigitFromUrl(url: String): Int {
    val cleanUrl = url.removeSuffix("/")
    val lastSegment = cleanUrl.substringAfterLast("/")
    return lastSegment.toInt()
}