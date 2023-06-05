package com.hvasoft.counterpro.domain.repository

import com.hvasoft.counterpro.core.common.Result
import com.hvasoft.counterpro.domain.model.Counter

interface CounterRepository {

    suspend fun getCountersRemote(): Result<List<Counter>>

    suspend fun getCountersLocal(): List<Counter>

    suspend fun getCounterByTitle(title: String): Counter?

    suspend fun insertCounterLocal(counter: Counter)

    suspend fun insertCounterRemote(title: String): Result<List<Counter>>

}