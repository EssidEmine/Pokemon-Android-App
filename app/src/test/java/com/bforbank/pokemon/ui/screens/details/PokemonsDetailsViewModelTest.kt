package com.bforbank.pokemon.ui.screens.details

import androidx.lifecycle.SavedStateHandle
import com.bforbank.pokemon.domain.models.PokemonDetails
import com.bforbank.pokemon.domain.models.PokemonDetailsError
import com.bforbank.pokemon.domain.usecases.GetPokemonDetailsUseCaseImpl
import com.bforbank.pokemon.ui.dispatchers.DispatcherProviderImpl
import com.bforbank.pokemon.ui.screens.details.mapper.PokemonDetailsUiModelMapper
import com.bforbank.pokemon.ui.screens.details.model.PokemonDetailsContentUiModel
import com.bforbank.pokemon.ui.screens.details.model.PokemonDetailsErrorUiModel
import com.bforbank.pokemon.ui.screens.details.model.PokemonDetailsUiModel
import com.bforbank.pokemon.ui.screens.details.model.PokemonDetailsUiModelType
import com.bforbank.pokemon.ui.screens.details.model.StatsUiModel
import com.bforbank.pokemon.utils.CoroutinesTestRule
import com.bforbank.pokemon.utils.Result
import com.bforbank.pokemon.utils.statehandlers.UiModelHandlerFactory
import com.bforbank.pokemon.utils.statehandlers.UiModelTestHandler
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any

class PokemonDetailsViewModelTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var savedStateHandle: SavedStateHandle

    @Mock
    private lateinit var getPokemonDetailsUseCaseImpl: GetPokemonDetailsUseCaseImpl

    @Mock
    private lateinit var pokemonDetailsUiModelMapper: PokemonDetailsUiModelMapper
    private val uiModelHandler = UiModelTestHandler(
        MutableStateFlow(PokemonDetailsUiModel())
    )

    @Mock
    private lateinit var uiModelHandlerFactory: UiModelHandlerFactory
    private lateinit var viewModel: PokemonDetailsViewModel
    private lateinit var closeable: AutoCloseable

    @Before
    fun setup() {
        closeable = MockitoAnnotations.openMocks(this)

        given(
            uiModelHandlerFactory.buildSavedStateUiStateHandler<PokemonDetailsUiModel>(
                savedStateHandle = any(),
                defaultUiModel = any(),
            )
        ).willReturn(uiModelHandler)
    }

    @After
    fun tearDown() {
        closeable.close()
    }

    @Test
    fun `loadData - Success`() = runTest {
        // Arrange
        val givenPokemonDetailsContentUiModel = PokemonDetailsContentUiModel(
            imageUrls = listOf(
                "https://example.com/pikachu_front.png",
                "https://example.com/pikachu_back.png"
            ),
            stats = listOf(
                StatsUiModel(
                    baseStat = 55,
                    effort = 0,
                    name = "speed"
                )
            ),
            height = 4,
            weight = 60
        )

        val givenPokemonDetailsUiModel = PokemonDetailsUiModel(
            content = givenPokemonDetailsContentUiModel
        )

        val pokemonDetails = PokemonDetails(
            height = 4,
            weight = 60,
            sprites = PokemonDetails.Sprites(
                frontDefault = "frontDefault",
                backDefault = "backDefault",
                backShiny = "backShiny",
                frontShiny = "frontShiny",
            ),
            stats = listOf(
                PokemonDetails.Stats(
                    baseStat = 55,
                    effort = 0,
                    stat = PokemonDetails.Stat(
                        name = "speed",
                        url = "https://example.com/speed"
                    )
                )
            )
        )
        given(savedStateHandle.get<Int>(any())).willReturn(1)
        given(getPokemonDetailsUseCaseImpl.invoke(1)).willReturn(
            flow {
                emit(
                    Result.Success(
                        pokemonDetails
                    )
                )
            })
        given(pokemonDetailsUiModelMapper.map(pokemonDetails)).willReturn(
            givenPokemonDetailsContentUiModel
        )
        // Act
        initViewModel()
        // Assert
        assertEquals(
            givenPokemonDetailsUiModel.copy(
                isLoading = false,
            ),
            uiModelHandler.lastValue
        )
    }

    @Test
    fun `loadData - GENERIC Error`() = runTest {
        // Arrange
        val errorMessage = "GENERIC error"
        given(savedStateHandle.get<Int>(any())).willReturn(1)
        given(getPokemonDetailsUseCaseImpl.invoke(1)).willReturn(flow {
            emit(
                Result.Error(
                    PokemonDetailsError.Generic(errorMessage)
                )
            )
        })
        // Act
        initViewModel()
        // Assert
        assertEquals(
            PokemonDetailsUiModel(
                isLoading = false,
                error = PokemonDetailsErrorUiModel(
                    label = errorMessage,
                    type = PokemonDetailsUiModelType.GENERIC
                )
            ),
            uiModelHandler.lastValue
        )
    }

    @Test
    fun `loadData - Network Error`() = runTest {
        // Arrange
        val errorMessage = "Network error"
        given(savedStateHandle.get<Int>(any())).willReturn(1)
        given(getPokemonDetailsUseCaseImpl.invoke(1)).willReturn(flow {
            emit(
                Result.Error(
                    PokemonDetailsError.Network(errorMessage)
                )
            )
        })
        // Act
        initViewModel()
        // Assert
        assertEquals(
            PokemonDetailsUiModel(
                isLoading = false,
                error = PokemonDetailsErrorUiModel(
                    label = errorMessage,
                    type = PokemonDetailsUiModelType.NETWORK
                )
            ),
            uiModelHandler.lastValue
        )
    }

    @Test
    fun `loadData - pokemonId Null Error`() = runTest {
        // Arrange
        given(savedStateHandle.get<Int>(any())).willReturn(null)
        // Act
        initViewModel()
        // Assert
        assertEquals(
            PokemonDetailsUiModel(
                isLoading = false,
                error = PokemonDetailsErrorUiModel(
                    label = "Pokemon Name is null error",
                    type = PokemonDetailsUiModelType.GENERIC
                )
            ),
            uiModelHandler.lastValue
        )
    }

    private fun initViewModel() {
        viewModel = PokemonDetailsViewModel(
            getPokemonDetailsUseCaseImpl = getPokemonDetailsUseCaseImpl,
            pokemonDetailsUiModelMapper = pokemonDetailsUiModelMapper,
            dispatcherProvider = DispatcherProviderImpl(
                main = coroutinesTestRule.testDispatcher,
                io = coroutinesTestRule.testDispatcher,
            ),
            savedStateHandle = savedStateHandle,
            uiModelHandlerFactory = uiModelHandlerFactory,
        )
    }
}
