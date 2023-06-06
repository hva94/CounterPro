package com.hvasoft.counterpro.domain.repository

import com.hvasoft.counterpro.core.common.Result
import com.hvasoft.counterpro.domain.model.Counter

interface CounterRemoteRepository {
    suspend fun getCounters(): Result<List<Counter>>
    suspend fun insertCounter(title: String): Result<List<Counter>>
    suspend fun incrementCounter(counter: Counter): Result<List<Counter>>
    suspend fun decrementCounter(counter: Counter): Result<List<Counter>>
    suspend fun deleteCounter(counter: Counter): Result<List<Counter>>
}