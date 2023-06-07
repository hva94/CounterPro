package com.hvasoft.counterpro.presentation.home

import com.google.common.truth.Truth.assertThat
import com.hvasoft.counterpro.R
import com.hvasoft.counterpro.core.common.Result
import com.hvasoft.counterpro.domain.model.Counter
import com.hvasoft.counterpro.domain.use_case.CounterUseCases
import com.hvasoft.counterpro.util.CoroutineRule
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    private val counter = Counter(
        id = "id_test",
        title = "Title test",
        count = 0
    )

    @get:Rule
    val coroutineRule = CoroutineRule()

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var useCases: CounterUseCases

    private lateinit var viewModel: HomeViewModel

    private val slot = slot<String>()

    @Before
    fun setUp() {
        viewModel = HomeViewModel(
            useCases,
            UnconfinedTestDispatcher()
        )
    }

    @Test
    fun getCounterState() {
        assertThat(viewModel.countersState.value).isInstanceOf(HomeState::class.java)
    }

    @Test
    fun `createCounter should return a list of counters`() = runTest {
        /** Given **/
        coEvery { useCases.createCounter(capture(slot)) } returns Result.Success(
            listOf(
                counter,
                counter.copy(id = "id_test_2", title = "Title test 2")
            )
        )
        val results = arrayListOf<HomeState>()

        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.countersState.toList(results)
        }

        /** When **/
        viewModel.createCounter("Title test")

        /** Then **/
        assertThat(slot.captured).isEqualTo("Title test")
        assertThat(results).isNotNull()
        assertThat(results).isNotEmpty()
        assertThat(results[1]).isEqualTo(HomeState.Loading)
        assertThat(results[2]).isInstanceOf(HomeState.Success::class.java)
        assertThat((results[2] as HomeState.Success).counters).isNotNull()
        assertThat((results[2] as HomeState.Success).counters).isNotEmpty()
        assertThat((results[2] as HomeState.Success).counters[0].title).isEqualTo("Title test")
        assertThat((results[2] as HomeState.Success).counters[1].title).isEqualTo("Title test 2")

        job.cancel()
    }

    @Test
    fun `createCounter should handle an error result`() = runTest {
        /** Given **/
        coEvery { useCases.createCounter(capture(slot)) } returns Result.Error(550)
        val results = arrayListOf<HomeState>()

        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.countersState.toList(results)
        }

        /** When **/
        viewModel.createCounter("Title test")

        /** Then **/
        assertThat(slot.captured).isEqualTo("Title test")
        assertThat(results).isNotNull()
        assertThat(results).isNotEmpty()
        assertThat(results[1]).isEqualTo(HomeState.Loading)
        assertThat(results[2]).isInstanceOf(HomeState.Failure::class.java)
        assertThat((results[2] as HomeState.Failure).error).isNotNull()
        assertThat((results[2] as HomeState.Failure).error?.errorMessageRes).isNotNull()
        assertThat((results[2] as HomeState.Failure).error?.errorMessageRes).isEqualTo(R.string.error_unknown)

        job.cancel()
    }

    @Test
    fun `createCounter should handle an exception result`() = runTest {
        /** Given **/
        coEvery { useCases.createCounter(capture(slot)) } returns Result.Exception(IOException())
        val results = arrayListOf<HomeState>()

        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.countersState.toList(results)
        }

        /** When **/
        viewModel.createCounter("Title test")

        /** Then **/
        assertThat(slot.captured).isEqualTo("Title test")
        assertThat(results).isNotNull()
        assertThat(results).isNotEmpty()
        assertThat(results[1]).isEqualTo(HomeState.Loading)
        assertThat(results[2]).isInstanceOf(HomeState.Failure::class.java)
        assertThat((results[2] as HomeState.Failure).error).isNotNull()
        assertThat((results[2] as HomeState.Failure).error?.errorMessageRes).isNotNull()
        assertThat((results[2] as HomeState.Failure).error?.errorMessageRes).isEqualTo(R.string.error_connectivity)

        job.cancel()
    }

    @Test
    fun `incrementCounter should return a list of counters`() = runTest {
        /** Given **/
        coEvery { useCases.incrementCounter(counter) } returns Result.Success(
            listOf(
                counter,
                counter.copy(id = "id_test_2", title = "Title test 2")
            )
        )
        val results = arrayListOf<HomeState>()

        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.countersState.toList(results)
        }

        /** When **/
        viewModel.incrementCounter(counter)

        /** Then **/
        assertThat(results).isNotNull()
        assertThat(results).isNotEmpty()
        assertThat(results[1]).isEqualTo(HomeState.Loading)
        assertThat(results[2]).isInstanceOf(HomeState.Success::class.java)
        assertThat((results[2] as HomeState.Success).counters).isNotNull()
        assertThat((results[2] as HomeState.Success).counters).isNotEmpty()
        assertThat((results[2] as HomeState.Success).counters[0].title).isEqualTo("Title test")
        assertThat((results[2] as HomeState.Success).counters[1].title).isEqualTo("Title test 2")

        job.cancel()
    }

    @Test
    fun `incrementCounter should handle an error result`() = runTest {
        /** Given **/
        coEvery { useCases.incrementCounter(counter) } returns Result.Error(550)
        val results = arrayListOf<HomeState>()

        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.countersState.toList(results)
        }

        /** When **/
        viewModel.incrementCounter(counter)

        /** Then **/
        assertThat(results).isNotNull()
        assertThat(results).isNotEmpty()
        assertThat(results[1]).isEqualTo(HomeState.Loading)
        assertThat(results[2]).isInstanceOf(HomeState.Failure::class.java)
        assertThat((results[2] as HomeState.Failure).error).isNotNull()
        assertThat((results[2] as HomeState.Failure).error?.errorMessageRes).isNotNull()
        assertThat((results[2] as HomeState.Failure).error?.errorMessageRes).isEqualTo(R.string.error_unknown)

        job.cancel()
    }

    @Test
    fun `incrementCounter should handle an exception result`() = runTest {
        /** Given **/
        coEvery { useCases.incrementCounter(counter) } returns Result.Exception(IOException())
        val results = arrayListOf<HomeState>()

        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.countersState.toList(results)
        }

        /** When **/
        viewModel.incrementCounter(counter)

        /** Then **/
        assertThat(results).isNotNull()
        assertThat(results).isNotEmpty()
        assertThat(results[1]).isEqualTo(HomeState.Loading)
        assertThat(results[2]).isInstanceOf(HomeState.Failure::class.java)
        assertThat((results[2] as HomeState.Failure).error).isNotNull()
        assertThat((results[2] as HomeState.Failure).error?.errorMessageRes).isNotNull()
        assertThat((results[2] as HomeState.Failure).error?.errorMessageRes).isEqualTo(R.string.error_connectivity)

        job.cancel()
    }

    @Test
    fun `decrementCounter should return a list of counters`() = runTest {
        /** Given **/
        coEvery { useCases.decrementCounter(counter) } returns Result.Success(
            listOf(
                counter,
                counter.copy(id = "id_test_2", title = "Title test 2")
            )
        )
        val results = arrayListOf<HomeState>()

        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.countersState.toList(results)
        }

        /** When **/
        viewModel.decrementCounter(counter)

        /** Then **/
        assertThat(results).isNotNull()
        assertThat(results).isNotEmpty()
        assertThat(results[1]).isEqualTo(HomeState.Loading)
        assertThat(results[2]).isInstanceOf(HomeState.Success::class.java)
        assertThat((results[2] as HomeState.Success).counters).isNotNull()
        assertThat((results[2] as HomeState.Success).counters).isNotEmpty()
        assertThat((results[2] as HomeState.Success).counters[0].title).isEqualTo("Title test")
        assertThat((results[2] as HomeState.Success).counters[1].title).isEqualTo("Title test 2")

        job.cancel()
    }

    @Test
    fun `decrementCounter should handle an error result`() = runTest {
        /** Given **/
        coEvery { useCases.decrementCounter(counter) } returns Result.Error(550)
        val results = arrayListOf<HomeState>()

        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.countersState.toList(results)
        }

        /** When **/
        viewModel.decrementCounter(counter)

        /** Then **/
        assertThat(results).isNotNull()
        assertThat(results).isNotEmpty()
        assertThat(results[1]).isEqualTo(HomeState.Loading)
        assertThat(results[2]).isInstanceOf(HomeState.Failure::class.java)
        assertThat((results[2] as HomeState.Failure).error).isNotNull()
        assertThat((results[2] as HomeState.Failure).error?.errorMessageRes).isNotNull()
        assertThat((results[2] as HomeState.Failure).error?.errorMessageRes).isEqualTo(R.string.error_unknown)

        job.cancel()
    }

    @Test
    fun `decrementCounter should handle an exception result`() = runTest {
        /** Given **/
        coEvery { useCases.decrementCounter(counter) } returns Result.Exception(IOException())
        val results = arrayListOf<HomeState>()

        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.countersState.toList(results)
        }

        /** When **/
        viewModel.decrementCounter(counter)

        /** Then **/
        assertThat(results).isNotNull()
        assertThat(results).isNotEmpty()
        assertThat(results[1]).isEqualTo(HomeState.Loading)
        assertThat(results[2]).isInstanceOf(HomeState.Failure::class.java)
        assertThat((results[2] as HomeState.Failure).error).isNotNull()
        assertThat((results[2] as HomeState.Failure).error?.errorMessageRes).isNotNull()
        assertThat((results[2] as HomeState.Failure).error?.errorMessageRes).isEqualTo(R.string.error_connectivity)

        job.cancel()
    }

    @Test
    fun `deleteCounters should return a list of counters`() = runTest {
        /** Given **/
        coEvery { useCases.deleteCounters(listOf(counter)) } returns Result.Success(
            listOf(
                counter,
                counter.copy(id = "id_test_2", title = "Title test 2")
            )
        )
        val results = arrayListOf<HomeState>()

        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.countersState.toList(results)
        }

        /** When **/
        viewModel.deleteCounters(listOf(counter))

        /** Then **/
        assertThat(results).isNotNull()
        assertThat(results).isNotEmpty()
        assertThat(results[1]).isEqualTo(HomeState.Loading)
        assertThat(results[2]).isInstanceOf(HomeState.Success::class.java)
        assertThat((results[2] as HomeState.Success).counters).isNotNull()
        assertThat((results[2] as HomeState.Success).counters).isNotEmpty()
        assertThat((results[2] as HomeState.Success).counters[0].title).isEqualTo("Title test")
        assertThat((results[2] as HomeState.Success).counters[1].title).isEqualTo("Title test 2")

        job.cancel()
    }

    @Test
    fun `deleteCounters should handle an error result`() = runTest {
        /** Given **/
        coEvery { useCases.deleteCounters(listOf(counter)) } returns Result.Error(550)
        val results = arrayListOf<HomeState>()

        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.countersState.toList(results)
        }

        /** When **/
        viewModel.deleteCounters(listOf(counter))

        /** Then **/
        assertThat(results).isNotNull()
        assertThat(results).isNotEmpty()
        assertThat(results[1]).isEqualTo(HomeState.Loading)
        assertThat(results[2]).isInstanceOf(HomeState.Failure::class.java)
        assertThat((results[2] as HomeState.Failure).error).isNotNull()
        assertThat((results[2] as HomeState.Failure).error?.errorMessageRes).isNotNull()
        assertThat((results[2] as HomeState.Failure).error?.errorMessageRes).isEqualTo(R.string.error_unknown)

        job.cancel()
    }

    @Test
    fun `deleteCounters should handle an exception result`() = runTest {
        /** Given **/
        coEvery { useCases.deleteCounters(listOf(counter)) } returns Result.Exception(IOException())
        val results = arrayListOf<HomeState>()

        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.countersState.toList(results)
        }

        /** When **/
        viewModel.deleteCounters(listOf(counter))

        /** Then **/
        assertThat(results).isNotNull()
        assertThat(results).isNotEmpty()
        assertThat(results[1]).isEqualTo(HomeState.Loading)
        assertThat(results[2]).isInstanceOf(HomeState.Failure::class.java)
        assertThat((results[2] as HomeState.Failure).error).isNotNull()
        assertThat((results[2] as HomeState.Failure).error?.errorMessageRes).isNotNull()
        assertThat((results[2] as HomeState.Failure).error?.errorMessageRes).isEqualTo(R.string.error_connectivity)

        job.cancel()
    }

}