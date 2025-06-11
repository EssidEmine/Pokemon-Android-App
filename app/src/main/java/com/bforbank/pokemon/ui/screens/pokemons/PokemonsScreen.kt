package com.bforbank.pokemon.ui.screens.pokemons

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bforbank.pokemon.ui.screens.error.ErrorScreen
import com.bforbank.pokemon.ui.screens.pokemons.model.PokemonUiModel
import com.bforbank.pokemon.ui.screens.pokemons.model.PokemonsErrorUiModelType.NETWORK
import com.bforbank.pokemon.ui.screens.pokemons.model.PokemonsErrorUiModelType.UNKNOWN
import com.bforbank.pokemon.ui.screens.pokemons.model.PokemonsNavigation
import com.bforbank.pokemon.ui.screens.pokemons.model.PokemonsUiModel
import com.bforbank.pokemon.utils.theme.BforTheme
import com.test.bfor.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonScreen(
    modifier: Modifier = Modifier,
    viewModel: PokemonsViewModel = hiltViewModel(),
    onShowDetails: (id: Int) -> Unit,
    onBackPressed: () -> Unit,
) {
    val uiState by viewModel.uiModelFlow.collectAsStateWithLifecycle()
    val searchedPokemon = remember { mutableStateOf(uiState.searchedPokemon) }

    BackHandler { onBackPressed() }

    when (val navigation = uiState.navigation) {
        is PokemonsNavigation.NONE -> {}
        is PokemonsNavigation.ShowDetails -> {
            onShowDetails(navigation.id)
            viewModel.resetNavigation()
        }
    }
    BforTheme {
        Scaffold(
            modifier = modifier,
            topBar = {
                SearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    placeholder = {
                        Text(text = stringResource(id = R.string.search_pokemons))
                    },
                    onQueryChange = {
                        searchedPokemon.value = it
                        viewModel.onSearchTextChange(it)
                    },
                    onSearch = viewModel::onSearchTextChange,
                    active = false,
                    query = searchedPokemon.value,
                    onActiveChange = {},
                    leadingIcon = {
                        Icon(Icons.Filled.Search, contentDescription = null)
                    },
                ) {}
            },
            content = { paddingValues ->
                if (uiState.isLoading) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    PokemonsContent(
                        modifier = Modifier.padding(paddingValues),
                        uiState,
                        onShowDetails,
                        viewModel::loadMore
                    )
                }

                uiState.error?.let {
                    ErrorScreen(
                        modifier = Modifier.padding(paddingValues),
                        error = it.label,
                        iconId = when (it.type) {
                            NETWORK -> R.drawable.cloud
                            UNKNOWN -> R.drawable.frog
                        }
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PokemonsContent(
    modifier: Modifier = Modifier,
    uiState: PokemonsUiModel,
    onShowDetails: (id: Int) -> Unit,
    onLoadMore: () -> Unit,
) {
    val listState = rememberLazyListState()
    val reachedBottom: Boolean by remember { derivedStateOf { listState.reachedBottom() } }

    LaunchedEffect(reachedBottom) {
        if (reachedBottom) onLoadMore()
    }

    uiState.pokemons?.let {
        val pokemons = it.groupBy { groupe ->
            groupe.name.first().uppercaseChar()
        }










        Column(
            Modifier
                .fillMaxWidth().padding(32.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(Modifier.height(200.dp))
            //ORIGINAL VIEW



            DrawableBankCard(
                cardNumber = "•••• •••• •••• 5678",
                cardHolder = "YOUR NAME",
                expiryDate = "06/28"
            )
        }










        /*
               LazyColumn(
                   modifier = modifier.fillMaxWidth(),
                   state = listState,
                   userScrollEnabled = true,
               ) {


                   pokemons.forEach { (letter, pokemonsStartingWithLetter) ->
                       stickyHeader {
                           Box(
                               modifier = Modifier
                                   .fillMaxWidth()
                                   .background(MaterialTheme.colorScheme.inverseSurface)
                           ) {
                               Text(
                                   text = "$letter",
                                   color = MaterialTheme.colorScheme.surface,
                                   fontSize = 18.sp,
                                   fontWeight = FontWeight.Bold,
                                   modifier = Modifier
                                       .padding(8.dp)
                               )
                           }
                       }

                       items(pokemonsStartingWithLetter.size) {
                           Column(
                               modifier = Modifier
                                   .fillMaxWidth()
                                   .padding(horizontal = 16.dp, vertical = 8.dp)
                                   .clickable { onShowDetails(pokemonsStartingWithLetter[it].id) },
                               verticalArrangement = Arrangement.Center,
                           ) {
                               Text(
                                   text = pokemonsStartingWithLetter[it].name,
                                   fontSize = 14.sp,
                                   color = MaterialTheme.colorScheme.onSurface,
                                   fontWeight = FontWeight.Bold,
                                   modifier = Modifier.padding(bottom = 4.dp)
                               )

                           }
                       }


            }
        }

         */
    }
}

private fun LazyListState.reachedBottom(buffer: Int = 1): Boolean {
    val lastVisibleItem = this.layoutInfo.visibleItemsInfo.lastOrNull()
    return lastVisibleItem?.index != 0 && lastVisibleItem?.index == this.layoutInfo.totalItemsCount - buffer
}

@Preview(
    name = "Light Mode",
    showBackground = true,
)
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_UNDEFINED,
    showBackground = true,
)
@Composable
private fun PokemonsContentPreview() {
    BforTheme {
        PokemonsContent(
            uiState = PokemonsUiModel(
                pokemons = listOf(
                    PokemonUiModel("Pokemon1", 1),
                    PokemonUiModel("Pokemon2", 2),
                    PokemonUiModel("Pokemon3", 3),
                )
            ),
            onShowDetails = {},
            onLoadMore = {},
        )
    }
}
