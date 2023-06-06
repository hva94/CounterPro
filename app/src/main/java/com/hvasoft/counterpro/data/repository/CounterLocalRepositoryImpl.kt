package com.hvasoft.counterpro.data.repository

import com.hvasoft.counterpro.core.util.toEntity
import com.hvasoft.counterpro.data.local_db.dao.CounterDao
import com.hvasoft.counterpro.domain.model.Counter
import com.hvasoft.counterpro.domain.repository.CounterLocalRepository
import javax.inject.Inject

class CounterLocalRepositoryImpl @Inject constructor(
    private val counterDao: CounterDao
) : CounterLocalRepository {

    override suspend fun getCounters(): List<Counter> {
        return counterDao.getCounters().map { counter -> counter.toDomain() }
    }

    override suspend fun getCounterByTitle(title: String): Counter? {
        return counterDao.getCounterByTitle(title)?.toDomain()
    }

    override suspend fun insertCounter(counter: Counter) {
        counterDao.insertCounter(counter.toEntity())
    }

    override suspend fun deleteCounter(counter: Counter) {
        counterDao.deleteCounter(counter.toEntity())
    }

}