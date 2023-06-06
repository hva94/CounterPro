package com.hvasoft.counterpro.di

import com.hvasoft.counterpro.data.local_db.dao.CounterDao
import com.hvasoft.counterpro.data.remote_db.service.CounterApi
import com.hvasoft.counterpro.data.repository.CounterLocalRepositoryImpl
import com.hvasoft.counterpro.data.repository.CounterRemoteRepositoryImpl
import com.hvasoft.counterpro.domain.repository.CounterLocalRepository
import com.hvasoft.counterpro.domain.repository.CounterRemoteRepository
import com.hvasoft.counterpro.domain.use_case.CounterUseCases
import com.hvasoft.counterpro.domain.use_case.CreateCounterUC
import com.hvasoft.counterpro.domain.use_case.DecrementCounterUC
import com.hvasoft.counterpro.domain.use_case.DeleteCountersUC
import com.hvasoft.counterpro.domain.use_case.GetCountersUC
import com.hvasoft.counterpro.domain.use_case.IncrementCounterUC
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
    fun providesCounterLocalRepository(
        counterDao: CounterDao
    ): CounterLocalRepository =
        CounterLocalRepositoryImpl(
            counterDao
        )

    @Provides
    @Singleton
    fun providesCounterRemoteRepository(
        counterApi: CounterApi
    ): CounterRemoteRepository =
        CounterRemoteRepositoryImpl(
            counterApi
        )

    @Provides
    @Singleton
    fun providesCounterUseCases(
        localRepository: CounterLocalRepository,
        remoteRepository: CounterRemoteRepository
    ): CounterUseCases = CounterUseCases(
        getCounters = GetCountersUC(localRepository, remoteRepository),
        createCounter = CreateCounterUC(localRepository, remoteRepository),
        incrementCounter = IncrementCounterUC(localRepository, remoteRepository),
        decrementCounter = DecrementCounterUC(localRepository, remoteRepository),
        deleteCounters = DeleteCountersUC(localRepository, remoteRepository)
    )

}