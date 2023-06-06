package com.hvasoft.counterpro.domain.use_case

import android.database.sqlite.SQLiteConstraintException
import com.hvasoft.counterpro.core.common.Result
import com.hvasoft.counterpro.core.common.fold
import com.hvasoft.counterpro.core.common.getSuccess
import com.hvasoft.counterpro.domain.model.Counter
import com.hvasoft.counterpro.domain.repository.CounterLocalRepository
import com.hvasoft.counterpro.domain.repository.CounterRemoteRepository
import javax.inject.Inject
import kotlin.math.abs

class CreateCounterUC @Inject constructor(
    private val localRepo: CounterLocalRepository,
    private val remoteRepo: CounterRemoteRepository
) {
    suspend operator fun invoke(title: String): Result<List<Counter>> {
        if (title.isBlank()) {
            return Result.Error(500)
        }
        val counterDuplicate = localRepo.getCounterByTitle(title)
        if (counterDuplicate != null) {
            return Result.Error(501)
        }
        return remoteRepo.insertCounter(title).fold(
            onSuccess = { counters ->
                val counterInserted = counters.find { it.title == title }
                if (counterInserted != null) {
                    try {
                        localRepo.insertCounter(counterInserted)
                        val localCounters = localRepo.getCounters()
                        remoteRepo.getCounters().getSuccess()?.forEach { remoteCounter ->
                            val localCounter = localCounters.find { it.title == remoteCounter.title }
                            if (localCounter != null) {
                                val diffLocalRemoteCounter = remoteCounter.count - localCounter.count
                                if (diffLocalRemoteCounter < 0) {
                                    repeat(abs(diffLocalRemoteCounter)) {
                                        remoteRepo.incrementCounter(localCounter)
                                    }
                                } else if (diffLocalRemoteCounter > 0) {
                                    repeat(diffLocalRemoteCounter) {
                                        remoteRepo.decrementCounter(localCounter)
                                    }
                                }
                            }
                        }
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
