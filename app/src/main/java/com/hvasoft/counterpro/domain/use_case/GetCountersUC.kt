package com.hvasoft.counterpro.domain.use_case

import android.database.sqlite.SQLiteConstraintException
import com.hvasoft.counterpro.core.common.Result
import com.hvasoft.counterpro.domain.model.Counter
import com.hvasoft.counterpro.domain.repository.CounterRepository
import javax.inject.Inject

class GetCountersUC @Inject constructor(
    private val repository: CounterRepository,
) {
    suspend operator fun invoke(): Result<List<Counter>> {
        return try {
            Result.Success(repository.getCountersLocal())
        } catch (e: SQLiteConstraintException) {
            Result.Exception(e)
        }
    }
}