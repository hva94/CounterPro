package com.hvasoft.counterpro.data.local_db.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.hvasoft.counterpro.data.util.DataConstants.TABLE_NAME_COUNTERS
import com.hvasoft.counterpro.data.util.DataConstants.INDEX_TITLE_VALUE
import com.hvasoft.counterpro.domain.model.Counter

@Entity(
    tableName = TABLE_NAME_COUNTERS,
    indices = [Index(value = [INDEX_TITLE_VALUE], unique = true)]
)
data class CounterEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val title: String,
    val count: Int
) {
    fun toDomain() = Counter(
        id = id,
        title = title,
        count = count
    )
}
