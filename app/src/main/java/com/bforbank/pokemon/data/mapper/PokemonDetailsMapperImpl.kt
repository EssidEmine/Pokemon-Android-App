package com.bforbank.pokemon.data.mapper

import com.bforbank.pokemon.data.model.PokemonDetailsDto
import com.bforbank.pokemon.domain.models.PokemonDetails
import com.bforbank.pokemon.domain.models.PokemonDetailsError
import com.bforbank.pokemon.utils.Result
import retrofit2.Response
import javax.inject.Inject

class PokemonDetailsMapperImpl @Inject constructor() : PokemonDetailsMapper {

    override fun map(response: Response<PokemonDetailsDto>): Result<PokemonDetails, PokemonDetailsError> {
        return if (response.isSuccessful) {

            response.body()?.let { pokemon ->
                Result.Success(
                    PokemonDetails(
                        sprites = PokemonDetails.Sprites(
                            backDefault = pokemon.sprites.backDefault,
                            backShiny = pokemon.sprites.backShiny,
                            frontDefault = pokemon.sprites.frontDefault,
                            frontShiny = pokemon.sprites.frontShiny
                        ),
                        stats = pokemon.stats.map { statDto ->
                            PokemonDetails.Stats(
                                baseStat = statDto.baseStat,
                                effort = statDto.effort,
                                stat = PokemonDetails.Stat(
                                    name = statDto.stat.name,
                                    url = statDto.stat.url
                                )
                            )
                        },
                        height = pokemon.height,
                        weight = pokemon.weight
                    )
                )
            } ?: Result.Error(PokemonDetailsError.Generic("Empty Body :("))

        } else {
            Result.Error(PokemonDetailsError.Network(response.message()))
        }
    }
}
