package com.hvasoft.counterpro.domain.use_case

import com.google.common.truth.Truth.assertThat
import com.hvasoft.counterpro.core.common.Result
import com.hvasoft.counterpro.core.common.getSuccess
import com.hvasoft.counterpro.domain.model.Counter
import com.hvasoft.counterpro.domain.repository.CounterLocalRepository
import com.hvasoft.counterpro.domain.repository.CounterRemoteRepository
import com.hvasoft.counterpro.util.CoroutineRule
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class CreateCounterUCTest {

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

    private lateinit var createCounterUC: CreateCounterUC

    @Before
    fun setUp() {
        createCounterUC = CreateCounterUC(localRepo, remoteRepo)
    }

    @Test
    fun `createCounter should return a title blank error`() = runTest {
        /** Given **/
        val title = ""

        /** When **/
        val result = createCounterUC(title)

        /** Then **/
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat(result).isNotNull()
        assertThat((result as Result.Error).code).isEqualTo(500)

        coVerify { localRepo wasNot Called }
        coVerify { remoteRepo wasNot Called }
    }

    @Test
    fun `createCounter should return a counterDuplicate error`() = runTest {
        /** Given **/
        val title = "Counter test"

        /** When **/
        val result = createCounterUC(title)

        /** Then **/
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat(result).isNotNull()
        assertThat((result as Result.Error).code).isEqualTo(501)

        coVerify { remoteRepo wasNot Called }
    }

    @Test
    fun `createCounter should return a connectivity error`() = runTest {
        /** Given **/
        val title = "Counter test"
        coEvery { remoteRepo.insertCounter(any()) } returns Result.Exception(IOException())

        /** When **/
        val result = createCounterUC(title)

        /** Then **/
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat(result).isNotNull()
        assertThat((result as Result.Error).code).isEqualTo(501)
    }

}