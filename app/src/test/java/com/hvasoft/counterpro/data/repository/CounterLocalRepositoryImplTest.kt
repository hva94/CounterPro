package com.hvasoft.counterpro.data.repository

import com.google.common.truth.Truth.assertThat
import com.hvasoft.counterpro.data.local_db.dao.CounterDao
import com.hvasoft.counterpro.data.local_db.entities.CounterEntity
import com.hvasoft.counterpro.domain.model.Counter
import com.hvasoft.counterpro.util.CoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CounterLocalRepositoryImplTest {

    private val counter = Counter(
        id = "id_test",
        title = "Title test",
        count = 0
    )

    private val counterEntity = CounterEntity(
        id = "id_test",
        title = "Title test",
        count = 0
    )

    @get:Rule
    val coroutineRule = CoroutineRule()

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var dao: CounterDao

    private lateinit var repositoryImpl: CounterLocalRepositoryImpl

    private val slot = slot<String>()

    @Before
    fun setUp() {
        repositoryImpl = CounterLocalRepositoryImpl(dao)
    }

    @Test
    fun `getCounters should return a list of counters`() = runTest {
        /** Given **/
        coEvery { dao.getCounters() } returns listOf(counterEntity)

        /** When **/
        val result = repositoryImpl.getCounters()

        /** Then **/
        assertThat(result).isNotEmpty()
        assertThat(result).isNotNull()
        assertThat(result.count()).isEqualTo(1)
        assertThat(result.first().id).isEqualTo(counterEntity.id)
        assertThat(result.first().title).isEqualTo(counterEntity.title)
        assertThat(result.first().count).isEqualTo(counterEntity.count)
        coVerify { dao.getCounters() }
    }

    @Test
    fun `getCounters should return an empty list of counters`() = runTest {
        /** Given **/
        coEvery { dao.getCounters() } returns emptyList()

        /** When **/
        val result = repositoryImpl.getCounters()

        /** Then **/
        assertThat(result).isEmpty()
        assertThat(result).isNotNull()
        coVerify { dao.getCounters() }
    }
    
    @Test
    fun `getCounterByTitle should return a counter`() = runTest {
        /** Given **/
        coEvery { dao.getCounterByTitle(capture(slot)) } returns counterEntity

        /** When **/
        val result = repositoryImpl.getCounterByTitle("Title test")

        /** Then **/
        assertThat(slot.captured).isEqualTo("Title test")
        assertThat(result).isNotNull()
        assertThat(result?.id).isEqualTo(counterEntity.id)
        assertThat(result?.title).isEqualTo(counterEntity.title)
        assertThat(result?.count).isEqualTo(counterEntity.count)
        coVerify { dao.getCounterByTitle(capture(slot)) }
    }

    @Test
    fun `getCounterByTitle should return null`() = runTest {
        /** Given **/
        coEvery { dao.getCounterByTitle(capture(slot)) } returns null

        /** When **/
        val result = repositoryImpl.getCounterByTitle("Title test")

        /** Then **/
        assertThat(slot.captured).isEqualTo("Title test")
        assertThat(result).isNull()
        coVerify { dao.getCounterByTitle(capture(slot)) }
    }

    @Test
    fun `insertCounter should insert a counter`() = runTest {
        /** Given **/
        coEvery { dao.insertCounter(counterEntity) } returns Unit

        /** When **/
        repositoryImpl.insertCounter(counter)

        /** Then **/
        coVerify { dao.insertCounter(counterEntity) }
    }

    @Test
    fun `deleteCounter should delete a counter`() = runTest {
        /** Given **/
        coEvery { dao.deleteCounter(counterEntity) } returns Unit

        /** When **/
        repositoryImpl.deleteCounter(counter)

        /** Then **/
        coVerify { dao.deleteCounter(counterEntity) }
    }
}