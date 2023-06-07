package com.hvasoft.counterpro.domain.use_case

import android.database.sqlite.SQLiteConstraintException
import com.google.common.truth.Truth.assertThat
import com.hvasoft.counterpro.core.common.Result
import com.hvasoft.counterpro.domain.model.Counter
import com.hvasoft.counterpro.domain.repository.CounterLocalRepository
import com.hvasoft.counterpro.domain.repository.CounterRemoteRepository
import com.hvasoft.counterpro.util.CoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class GetCountersUCTest {

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
    private lateinit var localRepo: CounterLocalRepository

    @RelaxedMockK
    private lateinit var remoteRepo: CounterRemoteRepository

    private lateinit var getCountersUC: GetCountersUC

    @Before
    fun setUp() {
        getCountersUC = GetCountersUC(localRepo, remoteRepo)
    }

    @Test
    fun `getCounters should return a list of counters`() = runTest {
        /** Given **/
        val mockSavedCounters = listOf(
            counter,
            counter.copy(id = "id_test_2", title = "Title test 2")
        )
        coEvery { localRepo.getCounters() } returns mockSavedCounters
        coEvery { remoteRepo.getCounters() } returns Result.Success(mockSavedCounters)

        /** When **/
        val result = getCountersUC()

        /** Then **/
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat(result).isNotNull()
        assertThat((result as Result.Success).data[0].title).isEqualTo("Title test")
        assertThat((result).data[1].title).isEqualTo("Title test 2")

        coVerify { localRepo.getCounters() }
        coVerify { remoteRepo.getCounters() }
    }

    @Test
    fun `getCounters should return an empty list`() = runTest {
        /** Given **/
        coEvery { localRepo.getCounters() } returns emptyList()
        coEvery { remoteRepo.getCounters() } returns Result.Exception(IOException())

        /** When **/
        val result = getCountersUC()

        /** Then **/
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat(result).isNotNull()
        assertThat((result as Result.Success).data).isEmpty()

        coVerify { localRepo.getCounters() }
        coVerify { remoteRepo.getCounters() }
    }

    @Test
    fun `getCounters should return an exception result`() = runTest {
        /** Given **/
        coEvery { localRepo.getCounters() } throws SQLiteConstraintException()

        /** When **/
        val result = getCountersUC()

        /** Then **/
        assertThat(result).isInstanceOf(Result.Exception::class.java)
        assertThat(result).isNotNull()
        assertThat((result as Result.Exception).exception).isInstanceOf(SQLiteConstraintException::class.java)

        coVerify { localRepo.getCounters() }
    }
}