package com.hvasoft.counterpro.data.local_db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hvasoft.counterpro.data.local_db.entities.CounterEntity

@Dao
interface CounterDao {

    @Query("SELECT * FROM counters WHERE isDeleted = 0 ORDER BY id ASC")
    suspend fun getCounters(): List<CounterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCounter(counter: CounterEntity)

    @Query("SELECT * FROM counters WHERE title = :title")
    suspend fun getCounterByTitle(title: String): CounterEntity?

    @Delete
    suspend fun deleteCounter(counter: CounterEntity)

}