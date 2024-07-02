package com.bforbank.pokemon.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bforbank.pokemon.ui.screens.details.PokemonDetailsScreen
import com.bforbank.pokemon.ui.screens.pokemons.PokemonScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Destination.Pokemons.route,
            ) {
                composable(route = Destination.Pokemons.route) {
                    PokemonScreen(
                        onBackPressed = {
                            finish()
                        },
                        onShowDetails = { id ->
                            navController.navigate(
                                route = Destination.PokemonsDetails.buildUri(id)
                            )
                        },
                    )
                }

                composable(
                    route = Destination.PokemonsDetails.route,
                    arguments = listOf(
                        Destination.PokemonsDetails.pokemonDetailsNavArgument()
                    )
                ) {
                    PokemonDetailsScreen(
                        onBackPressed = {
                            navController.popBackStack(
                                route = Destination.Pokemons.route,
                                inclusive = false
                            )
                        },
                    )
                }
            }
        }
    }
}
