package com.hvasoft.counterpro.di

import com.hvasoft.counterpro.data.local_db.dao.CounterDao
import com.hvasoft.counterpro.data.remote_db.service.CounterApi
import com.hvasoft.counterpro.data.repository.CounterRepositoryImpl
import com.hvasoft.counterpro.domain.repository.CounterRepository
import com.hvasoft.counterpro.domain.use_case.CounterUseCases
import com.hvasoft.counterpro.domain.use_case.CreateCounterUC
import com.hvasoft.counterpro.domain.use_case.GetCountersUC
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providesCounterRepository(
        counterApi: CounterApi,
        counterDao: CounterDao
    ): CounterRepository =
        CounterRepositoryImpl(
            counterApi,
            counterDao
        )

    @Provides
    @Singleton
    fun providesCounterUseCases(
        counterRepository: CounterRepository
    ): CounterUseCases = CounterUseCases(
        getCounters = GetCountersUC(counterRepository),
        createCounter = CreateCounterUC(counterRepository)
    )

}