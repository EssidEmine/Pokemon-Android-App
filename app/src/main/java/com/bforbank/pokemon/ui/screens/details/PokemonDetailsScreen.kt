package com.bforbank.pokemon.ui.screens.details

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.bforbank.pokemon.ui.screens.details.model.PokemonDetailsContentUiModel
import com.bforbank.pokemon.ui.screens.details.model.PokemonDetailsUiModelType
import com.bforbank.pokemon.ui.screens.details.model.StatsUiModel
import com.bforbank.pokemon.ui.screens.error.ErrorScreen
import com.bforbank.pokemon.utils.theme.BforTheme
import com.test.bfor.R

@Composable
fun PokemonDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: PokemonDetailsViewModel = hiltViewModel(),
    onBackPressed: () -> Unit,
) {
    val uiState by viewModel.uiModelFlow.collectAsStateWithLifecycle()
    BackHandler {
        onBackPressed()
    }
    BforTheme {
        Scaffold(
            modifier = modifier,
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                ) {
                    if (uiState.isLoading) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    uiState.error?.let {
                        ErrorScreen(
                            error = it.label,
                            iconId = when (it.type) {
                                PokemonDetailsUiModelType.NETWORK -> R.drawable.cloud
                                else -> R.drawable.frog
                            }
                        )
                    }

                    uiState.content?.let {
                        DetailsContent(
                            uiModel = it
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun DetailsContent(
    modifier: Modifier = Modifier,
    uiModel: PokemonDetailsContentUiModel,
) {
    LazyColumn(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.pokemon_height, uiModel.height),
                    style = MaterialTheme.typography.h5
                )
                Text(
                    text = stringResource(R.string.pokemon_weight, uiModel.weight),
                    style = MaterialTheme.typography.h5
                )
            }
        }
        item {
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                uiModel.imageUrls.forEach { imageUrl ->
                    AsyncImage(
                        modifier = Modifier.size(100.dp),
                        model = imageUrl,
                        contentDescription = "Pokemon Image"
                    )
                }
            }
        }

        item {
            Text(
                modifier = Modifier,
                text = stringResource(R.string.pokemon_stats),
                style = MaterialTheme.typography.h5,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.height(8.dp))
            Divider()
        }

        items(uiModel.stats.size) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = uiModel.stats[it].name,
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.pokemon_base_stat, uiModel.stats[it].baseStat),
                    style = MaterialTheme.typography.body2
                )
                Text(
                    text = stringResource(R.string.pokemon_effort, uiModel.stats[it].effort),
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
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
fun PreviewPokemonDetailsScreen() {
    val uiModel = PokemonDetailsContentUiModel(
        imageUrls = listOf(
            "1.png",
            "2.png"
        ),
        stats = listOf(
            StatsUiModel(baseStat = 45, effort = 0, name = "HP"),
            StatsUiModel(baseStat = 49, effort = 0, name = "Attack")
        ),
        height = 7,
        weight = 69
    )
    BforTheme {
        DetailsContent(uiModel = uiModel)
    }
}
