package com.hvasoft.counterpro.domain.use_case

import android.database.sqlite.SQLiteConstraintException
import com.hvasoft.counterpro.core.common.Result
import com.hvasoft.counterpro.core.common.fold
import com.hvasoft.counterpro.domain.model.Counter
import com.hvasoft.counterpro.domain.repository.CounterRepository
import javax.inject.Inject

class CreateCounterUC @Inject constructor(
    private val repository: CounterRepository
) {
    suspend operator fun invoke(title: String): Result<List<Counter>> {
        if (title.isBlank()) {
            return Result.Error(500)
        }
        val counterDuplicate = repository.getCounterByTitle(title)
        if (counterDuplicate != null) {
            return Result.Error(501)
        }
        return repository.insertCounterRemote(title).fold(
            onSuccess = { counters ->
                val counterInserted = counters.find { it.title == title }
                if (counterInserted != null) {
                    try {
                        repository.insertCounterLocal(counterInserted)
                        val localCounters = repository.getCountersLocal()
                        Result.Success(localCounters)
                    } catch (e: SQLiteConstraintException) {
                        Result.Exception(e)
                    }
                } else {
                    Result.Error(502)
                }
            },
            onError = { code ->
                Result.Error(code)
            },
            onException = {
                Result.Error(502)
            })
    }
}
