package com.bforbank.pokemon.ui.screens.pokemons

import androidx.lifecycle.SavedStateHandle
import com.bforbank.pokemon.domain.models.Pokemon
import com.bforbank.pokemon.domain.models.Pokemons
import com.bforbank.pokemon.domain.models.PokemonsError
import com.bforbank.pokemon.domain.usecases.GetPokemonsUseCaseImpl
import com.bforbank.pokemon.ui.dispatchers.DispatcherProviderImpl
import com.bforbank.pokemon.ui.screens.pokemons.mapper.PokemonsUiModelMapper
import com.bforbank.pokemon.ui.screens.pokemons.model.PokemonUiModel
import com.bforbank.pokemon.ui.screens.pokemons.model.PokemonsErrorUiModel
import com.bforbank.pokemon.ui.screens.pokemons.model.PokemonsErrorUiModelType
import com.bforbank.pokemon.ui.screens.pokemons.model.PokemonsNavigation
import com.bforbank.pokemon.ui.screens.pokemons.model.PokemonsUiModel
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

class PokemonsViewModelTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var savedStateHandle: SavedStateHandle

    @Mock
    private lateinit var getPokemonsUseCase: GetPokemonsUseCaseImpl

    @Mock
    private lateinit var pokemonsUiModelMapper: PokemonsUiModelMapper
    private val uiModelHandler = UiModelTestHandler(
        MutableStateFlow(PokemonsUiModel())
    )

    @Mock
    private lateinit var uiModelHandlerFactory: UiModelHandlerFactory
    private lateinit var viewModel: PokemonsViewModel
    private lateinit var closeable: AutoCloseable

    @Before
    fun setup() {
        closeable = MockitoAnnotations.openMocks(this)

        given(
            uiModelHandlerFactory.buildSavedStateUiStateHandler<PokemonsUiModel>(
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
    fun `init - Success`() = runTest {
        // Arrange
        val givenPokemonUiModel = PokemonUiModel(
            name = "Pikachu",
            id = 1
        )
        val givenPokemons = Pokemons(
            content = listOf(
                Pokemon(
                    id = 1,
                    name = "Pikachu"
                )
            )
        )
        given(getPokemonsUseCase.invoke(any(), any())).willReturn(flow {
            emit(
                Result.Success(
                    givenPokemons
                )
            )
        })
        given(pokemonsUiModelMapper.map(givenPokemons)).willReturn(listOf(givenPokemonUiModel))

        // Act
        initViewModel()

        // Assert
        assertEquals(
            PokemonsUiModel(
                pokemons = listOf(givenPokemonUiModel),
                isLoading = false
            ),
            uiModelHandler.lastValue
        )
    }

    @Test
    fun `init - Network Error`() = runTest {
        // Arrange
        val errorMessage = "Network error"
        given(getPokemonsUseCase.invoke(any(), any())).willReturn(flow {
            emit(Result.Error(PokemonsError.Network(errorMessage)))
        })

        // Act
        initViewModel()

        // Assert
        assertEquals(
            PokemonsUiModel(
                isLoading = false,
                error = PokemonsErrorUiModel(
                    label = errorMessage,
                    type = PokemonsErrorUiModelType.NETWORK
                )
            ),
            uiModelHandler.lastValue
        )
    }

    @Test
    fun `init - Unknown Error`() = runTest {
        // Arrange
        val errorMessage = "Unknown error"
        given(getPokemonsUseCase.invoke(any(), any())).willReturn(flow {
            emit(Result.Error(PokemonsError.Unknown(errorMessage)))
        })

        // Act
        initViewModel()

        // Assert
        assertEquals(
            PokemonsUiModel(
                isLoading = false,
                error = PokemonsErrorUiModel(
                    label = errorMessage,
                    type = PokemonsErrorUiModelType.UNKNOWN
                )
            ),
            uiModelHandler.lastValue
        )
    }

    @Test
    fun `loadMore - Success`() = runTest {
        // Arrange
        val initialPokemon = Pokemon(id = 1, name = "Pikachu")
        val newPokemon = Pokemon(id = 2, name = "Charmander")
        val givenPokemons = Pokemons(content = listOf(initialPokemon))
        val newPokemons = Pokemons(content = listOf(newPokemon))
        val flow1 = flow { emit(Result.Success(givenPokemons)) }
        val flow2 = flow { emit(Result.Success(newPokemons)) }
        given(getPokemonsUseCase.invoke(any(), any()))
            .willReturn(flow1)
            .willReturn(flow2)
        given(pokemonsUiModelMapper.map(givenPokemons)).willReturn(
            listOf(
                PokemonUiModel(
                    name = "Pikachu",
                    id = 1
                )
            )
        )
        given(pokemonsUiModelMapper.map(newPokemons)).willReturn(
            listOf(
                PokemonUiModel(
                    name = "Charmander",
                    id = 2
                )
            )
        )

        // Act
        initViewModel()
        viewModel.loadMore()

        // Assert
        assertEquals(
            PokemonsUiModel(
                pokemons = listOf(
                    PokemonUiModel(name = "Charmander", id = 2),
                    PokemonUiModel(name = "Pikachu", id = 1)
                ),
                isLoading = false
            ),
            uiModelHandler.lastValue
        )
    }

    @Test
    fun `onSearchTextChange - Filter Pokemons`() = runTest {
        // Arrange
        val givenPokemons = Pokemons(
            content = listOf(
                Pokemon(id = 1, name = "Pikachu"),
                Pokemon(id = 2, name = "Charmander")
            )
        )
        given(getPokemonsUseCase.invoke(any(), any())).willReturn(flow {
            emit(
                Result.Success(
                    givenPokemons
                )
            )
        })
        given(pokemonsUiModelMapper.map(any())).willReturn(
            listOf(
                PokemonUiModel(
                    name = "Pikachu",
                    id = 1
                )
            )
        )

        // Act
        initViewModel()
        viewModel.onSearchTextChange("Pikachu")

        // Assert
        assertEquals(
            PokemonsUiModel(
                pokemons = listOf(
                    PokemonUiModel(
                        name = "Pikachu",
                        id = 1
                    )
                ),
                searchedPokemon = "Pikachu"
            ),
            uiModelHandler.lastValue
        )
    }

    @Test
    fun `showPokemonDetails - Navigate to Details`() = runTest {
        // Arrange
        given(getPokemonsUseCase.invoke(any(), any())).willReturn(flow {
            emit(
                Result.Success(
                    Pokemons(emptyList())
                )
            )
        })
        initViewModel()

        // Act
        viewModel.showPokemonDetails(1)

        // Assert
        assertEquals(
            PokemonsUiModel(
                pokemons = emptyList(),
                navigation = PokemonsNavigation.ShowDetails(1)
            ),
            uiModelHandler.lastValue
        )
    }

    @Test
    fun `resetNavigation - Reset Navigation`() = runTest {
        // Arrange
        given(getPokemonsUseCase.invoke(any(), any())).willReturn(flow {
            emit(
                Result.Success(
                    Pokemons(emptyList())
                )
            )
        })
        initViewModel()
        viewModel.showPokemonDetails(1)

        // Act
        viewModel.resetNavigation()

        // Assert
        assertEquals(
            PokemonsUiModel(
                pokemons = emptyList(),
                navigation = PokemonsNavigation.NONE
            ),
            uiModelHandler.lastValue
        )
    }

    private fun initViewModel() {
        viewModel = PokemonsViewModel(
            getPokemonsUseCase = getPokemonsUseCase,
            dispatcherProvider = DispatcherProviderImpl(
                main = coroutinesTestRule.testDispatcher,
                io = coroutinesTestRule.testDispatcher
            ),
            pokemonsUiModelMapper = pokemonsUiModelMapper,
            uiModelHandlerFactory = uiModelHandlerFactory,
            savedStateHandle = savedStateHandle
        )
    }
}
