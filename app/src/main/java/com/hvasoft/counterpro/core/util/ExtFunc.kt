package com.hvasoft.counterpro.core.util

import com.hvasoft.counterpro.data.local_db.entities.CounterEntity
import com.hvasoft.counterpro.data.remote_db.response.CounterResponseDTO.CounterDTO
import com.hvasoft.counterpro.domain.model.Counter

fun CounterDTO.toDomain(): Counter = Counter(
        id = id,
        title = title,
        count = count
    )

fun Counter.toEntity(): CounterEntity = CounterEntity(
        id = id,
        title = title,
        count = count,
        isDeleted = isDeleted
    )