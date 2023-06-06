package com.hvasoft.counterpro.domain.repository

import com.hvasoft.counterpro.domain.model.Counter

interface CounterLocalRepository {
    suspend fun getCounters(): List<Counter>
    suspend fun getCounterByTitle(title: String): Counter?
    suspend fun insertCounter(counter: Counter)
}