package com.hvasoft.counterpro.data.local_db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hvasoft.counterpro.data.local_db.dao.CounterDao
import com.hvasoft.counterpro.data.local_db.entities.CounterEntity

@Database(
    entities = [CounterEntity::class],
    version = 1
)
abstract class CounterDatabase : RoomDatabase() {

    abstract fun counterDao(): CounterDao

}