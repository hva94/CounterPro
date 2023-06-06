package com.hvasoft.counterpro.domain.use_case

data class CounterUseCases(
    val getCounters: GetCountersUC,
    val createCounter: CreateCounterUC,
    val incrementCounter: IncrementCounterUC,
    val decrementCounter: DecrementCounterUC
)
