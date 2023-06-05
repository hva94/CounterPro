package com.hvasoft.counterpro.di

import android.content.Context
import androidx.room.Room
import com.hvasoft.counterpro.data.local_db.CounterDatabase
import com.hvasoft.counterpro.data.util.DataConstants.DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesCounterDatabase(@ApplicationContext context: Context): CounterDatabase =
        Room.databaseBuilder(
            context,
            CounterDatabase::class.java,
            DB_NAME
        ).build()

    @Provides
    @Singleton
    fun providesCounterDao(counterDatabase: CounterDatabase) = counterDatabase.counterDao()

}