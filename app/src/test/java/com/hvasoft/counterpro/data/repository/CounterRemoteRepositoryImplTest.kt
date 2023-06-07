package com.hvasoft.counterpro.data.repository

import com.google.common.truth.Truth.assertThat
import com.hvasoft.counterpro.core.common.Result
import com.hvasoft.counterpro.data.remote_db.response.CounterResponseDTO
import com.hvasoft.counterpro.data.remote_db.service.CounterApi
import com.hvasoft.counterpro.domain.model.Counter
import com.hvasoft.counterpro.util.CoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.verifyAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class CounterRemoteRepositoryImplTest {

    private val counter = Counter(
        id = "id_test",
        title = "Title test",
        count = 0
    )

    private val counterDTO = CounterResponseDTO.CounterDTO(
        id = "id_test",
        title = "Title test",
        count = 0
    )

    private val counterResponseDTO = CounterResponseDTO(
        counters = listOf(counterDTO)
    )

    @get:Rule
    val coroutineRule = CoroutineRule()

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var api: CounterApi

    private lateinit var repositoryImpl: CounterRemoteRepositoryImpl

    @Before
    fun setUp() {
        repositoryImpl = CounterRemoteRepositoryImpl(api)
    }

    @Test
    fun `getCounters should be successful`() = runTest {
        /** Given **/
        val response = mockk<Response<List<CounterResponseDTO.CounterDTO>>>()
        every { response.isSuccessful } returns true
        every { response.body() } returns listOf(counterResponseDTO.counters.first())
        coEvery { api.getCounters() } returns response

        /** When **/
        val result = repositoryImpl.getCounters()

        /** Then **/
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isNotNull()
        assertThat(result.data).isNotEmpty()
        assertThat((result).data.first().id).isEqualTo("id_test")
        assertThat((result).data.first().title).isEqualTo("Title test")
        assertThat((result).data.first().count).isEqualTo(0)
        verifyAll {
            response.body()
            response.isSuccessful
        }
        coVerify { api.getCounters() }
    }

    @Test
    fun `getCounters should return an error result`() = runTest {
        /** Given **/
        val response = mockk<Response<List<CounterResponseDTO.CounterDTO>>>()
        every { response.isSuccessful } returns false
        every { response.body() } returns null
        every { response.code() } returns 404
        coEvery { api.getCounters() } returns response

        /** When **/
        val result = repositoryImpl.getCounters()

        /** Then **/
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).code).isEqualTo(404)
        verifyAll {
            response.body()
            response.isSuccessful
            response.code()
        }
        coVerify { api.getCounters() }
    }

    @Test
    fun `getCounters should return an exception result`() = runTest {
        /** Given **/
        val exception = mockk<Exception>()
        coEvery { api.getCounters() } throws exception

        /** When **/
        val result = repositoryImpl.getCounters()

        /** Then **/
        assertThat(result).isInstanceOf(Result.Exception::class.java)
        assertThat((result as Result.Exception).exception).isEqualTo(exception)
        coVerify { api.getCounters() }
    }

    @Test
    fun `insertCounter should be successful`() = runTest {
        /** Given **/
        val response = mockk<Response<List<CounterResponseDTO.CounterDTO>>>()
        every { response.isSuccessful } returns true
        every { response.body() } returns listOf(counterResponseDTO.counters.first())
        coEvery { api.insertCounter(any()) } returns response

        /** When **/
        val result = repositoryImpl.insertCounter("Title test")

        /** Then **/
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isNotNull()
        assertThat(result.data.first().id).isEqualTo("id_test")
        assertThat(result.data.first().title).isEqualTo("Title test")
        assertThat(result.data.first().count).isEqualTo(0)
        verifyAll {
            response.body()
            response.isSuccessful
        }
        coVerify { api.insertCounter(any()) }
    }

    @Test
    fun `insertCounter should return an error result`() = runTest {
        /** Given **/
        val response = mockk<Response<List<CounterResponseDTO.CounterDTO>>>()
        every { response.isSuccessful } returns false
        every { response.body() } returns null
        every { response.code() } returns 404
        coEvery { api.insertCounter(any()) } returns response

        /** When **/
        val result = repositoryImpl.insertCounter("Title test")

        /** Then **/
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).code).isEqualTo(404)
        verifyAll {
            response.body()
            response.isSuccessful
            response.code()
        }
        coVerify { api.insertCounter(any()) }
    }

    @Test
    fun `insertCounter should return an exception result`() = runTest {
        /** Given **/
        val exception = mockk<Exception>()
        coEvery { api.insertCounter(any()) } throws exception

        /** When **/
        val result = repositoryImpl.insertCounter("Title test")

        /** Then **/
        assertThat(result).isInstanceOf(Result.Exception::class.java)
        assertThat((result as Result.Exception).exception).isEqualTo(exception)
        coVerify { api.insertCounter(any()) }
    }

    @Test
    fun `incrementCounter should be successful`() = runTest {
        /** Given **/
        val response = mockk<Response<List<CounterResponseDTO.CounterDTO>>>()
        every { response.isSuccessful } returns true
        every { response.body() } returns listOf(counterResponseDTO.counters.first())
        coEvery { api.incrementCounter(any()) } returns response

        /** When **/
        val result = repositoryImpl.incrementCounter(counter)

        /** Then **/
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isNotNull()
        assertThat(result.data.first().id).isEqualTo("id_test")
        assertThat(result.data.first().title).isEqualTo("Title test")
        assertThat(result.data.first().count).isEqualTo(0)
        verifyAll {
            response.body()
            response.isSuccessful
        }
        coVerify { api.incrementCounter(any()) }
    }

    @Test
    fun `incrementCounter should return an error result`() = runTest {
        /** Given **/
        val response = mockk<Response<List<CounterResponseDTO.CounterDTO>>>()
        every { response.isSuccessful } returns false
        every { response.body() } returns null
        every { response.code() } returns 404
        coEvery { api.incrementCounter(any()) } returns response

        /** When **/
        val result = repositoryImpl.incrementCounter(counter)

        /** Then **/
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).code).isEqualTo(404)
        verifyAll {
            response.body()
            response.isSuccessful
            response.code()
        }
        coVerify { api.incrementCounter(any()) }
    }

    @Test
    fun `incrementCounter should return an exception result`() = runTest {
        /** Given **/
        val exception = mockk<Exception>()
        coEvery { api.incrementCounter(any()) } throws exception

        /** When **/
        val result = repositoryImpl.incrementCounter(counter)

        /** Then **/
        assertThat(result).isInstanceOf(Result.Exception::class.java)
        assertThat((result as Result.Exception).exception).isEqualTo(exception)
        coVerify { api.incrementCounter(any()) }
    }

    @Test
    fun `decrementCounter should be successful`() = runTest {
        /** Given **/
        val response = mockk<Response<List<CounterResponseDTO.CounterDTO>>>()
        every { response.isSuccessful } returns true
        every { response.body() } returns listOf(counterResponseDTO.counters.first())
        coEvery { api.decrementCounter(any()) } returns response

        /** When **/
        val result = repositoryImpl.decrementCounter(counter)

        /** Then **/
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isNotNull()
        assertThat(result.data.first().id).isEqualTo("id_test")
        assertThat(result.data.first().title).isEqualTo("Title test")
        assertThat(result.data.first().count).isEqualTo(0)
        verifyAll {
            response.body()
            response.isSuccessful
        }
        coVerify { api.decrementCounter(any()) }
    }

    @Test
    fun `decrementCounter should return an error result`() = runTest {
        /** Given **/
        val response = mockk<Response<List<CounterResponseDTO.CounterDTO>>>()
        every { response.isSuccessful } returns false
        every { response.body() } returns null
        every { response.code() } returns 404
        coEvery { api.decrementCounter(any()) } returns response

        /** When **/
        val result = repositoryImpl.decrementCounter(counter)

        /** Then **/
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).code).isEqualTo(404)
        verifyAll {
            response.body()
            response.isSuccessful
            response.code()
        }
        coVerify { api.decrementCounter(any()) }
    }

    @Test
    fun `decrementCounter should return an exception result`() = runTest {
        /** Given **/
        val exception = mockk<Exception>()
        coEvery { api.decrementCounter(any()) } throws exception

        /** When **/
        val result = repositoryImpl.decrementCounter(counter)

        /** Then **/
        assertThat(result).isInstanceOf(Result.Exception::class.java)
        assertThat((result as Result.Exception).exception).isEqualTo(exception)
        coVerify { api.decrementCounter(any()) }
    }

    @Test
    fun `deleteCounter should be successful`() = runTest {
        /** Given **/
        val response = mockk<Response<List<CounterResponseDTO.CounterDTO>>>()
        every { response.isSuccessful } returns true
        every { response.body() } returns listOf(counterResponseDTO.counters.first())
        coEvery { api.deleteCounter(any()) } returns response

        /** When **/
        val result = repositoryImpl.deleteCounter(counter)

        /** Then **/
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isNotNull()
        assertThat(result.data.first().id).isEqualTo("id_test")
        assertThat(result.data.first().title).isEqualTo("Title test")
        assertThat(result.data.first().count).isEqualTo(0)
        verifyAll {
            response.body()
            response.isSuccessful
        }
        coVerify { api.deleteCounter(any()) }
    }

    @Test
    fun `deleteCounter should return an error result`() = runTest {
        /** Given **/
        val response = mockk<Response<List<CounterResponseDTO.CounterDTO>>>()
        every { response.isSuccessful } returns false
        every { response.body() } returns null
        every { response.code() } returns 404
        coEvery { api.deleteCounter(any()) } returns response

        /** When **/
        val result = repositoryImpl.deleteCounter(counter)

        /** Then **/
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).code).isEqualTo(404)
        verifyAll {
            response.body()
            response.isSuccessful
            response.code()
        }
        coVerify { api.deleteCounter(any()) }
    }

    @Test
    fun `deleteCounter should return an exception result`() = runTest {
        /** Given **/
        val exception = mockk<Exception>()
        coEvery { api.deleteCounter(any()) } throws exception

        /** When **/
        val result = repositoryImpl.deleteCounter(counter)

        /** Then **/
        assertThat(result).isInstanceOf(Result.Exception::class.java)
        assertThat((result as Result.Exception).exception).isEqualTo(exception)
        coVerify { api.deleteCounter(any()) }
    }
}