package com.hvasoft.counterpro.domain.use_case

import android.database.sqlite.SQLiteConstraintException
import com.google.common.truth.Truth.assertThat
import com.hvasoft.counterpro.domain.model.Counter
import com.hvasoft.counterpro.core.common.Result
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

@ExperimentalCoroutinesApi
class DecrementCounterUCTest {

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

    private lateinit var decrementCounterUC: DecrementCounterUC

    @Before
    fun setUp() {
        decrementCounterUC = DecrementCounterUC(localRepo, remoteRepo)
    }

    @Test
    fun `decrementCounterUC should return a list of counters`() = runTest {
        /** Given **/
        val mockSavedCounters = listOf(
            counter,
            counter.copy(id = "id_test_2", title = "Title test 2")
        )
        coEvery { localRepo.getCounters() } returns mockSavedCounters

        /** When **/
        val result = decrementCounterUC(counter)

        /** Then **/
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat(result).isNotNull()
        assertThat((result as Result.Success).data).isNotNull()
        assertThat(result.data).isEqualTo(mockSavedCounters)

        coVerify { localRepo.getCounters() }
    }

    @Test
    fun `getCounters should return an exception result`() = runTest {
        /** Given **/
        coEvery { localRepo.getCounters() } throws SQLiteConstraintException()

        /** When **/
        val result = decrementCounterUC(counter)

        /** Then **/
        assertThat(result).isInstanceOf(Result.Exception::class.java)
        assertThat(result).isNotNull()
        assertThat((result as Result.Exception).exception).isNotNull()
        assertThat(result.exception).isInstanceOf(SQLiteConstraintException::class.java)

        coVerify { localRepo.getCounters() }
    }

    @Test
    fun `insertCounter should return an exception result`() = runTest {
        /** Given **/
        coEvery { localRepo.insertCounter(counter) } throws SQLiteConstraintException()

        /** When **/
        val result = decrementCounterUC(counter)

        /** Then **/
        assertThat(result).isInstanceOf(Result.Exception::class.java)
        assertThat(result).isNotNull()
        assertThat((result as Result.Exception).exception).isNotNull()
        assertThat(result.exception).isInstanceOf(SQLiteConstraintException::class.java)

        coVerify { localRepo.insertCounter(counter) }
    }
}