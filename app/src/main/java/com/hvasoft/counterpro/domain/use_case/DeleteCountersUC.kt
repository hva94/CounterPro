package com.hvasoft.counterpro.domain.use_case

import android.database.sqlite.SQLiteConstraintException
import com.hvasoft.counterpro.core.common.Result
import com.hvasoft.counterpro.core.common.getSuccess
import com.hvasoft.counterpro.domain.model.Counter
import com.hvasoft.counterpro.domain.repository.CounterLocalRepository
import com.hvasoft.counterpro.domain.repository.CounterRemoteRepository
import javax.inject.Inject
import kotlin.math.abs

class DeleteCountersUC @Inject constructor(
    private val localRepo: CounterLocalRepository,
    private val remoteRepo: CounterRemoteRepository
) {
    suspend operator fun invoke(counters: List<Counter>): Result<List<Counter>> {
        try {
            counters.forEach { counter ->
                counter.isDeleted = true
                localRepo.insertCounter(counter)
            }
        } catch (e: SQLiteConstraintException) {
            return Result.Exception(e)
        }
        return try {
            val localCounters = localRepo.getCounters()
            remoteRepo.getCounters().getSuccess()?.forEach { remoteCounter ->
                val localCounter = localCounters.find { it.title == remoteCounter.title }
                if (localCounter != null) {
                    val diffRemoteLocalCounter = remoteCounter.count - localCounter.count
                    if (diffRemoteLocalCounter < 0) {
                        repeat(abs(diffRemoteLocalCounter)) {
                            remoteRepo.incrementCounter(localCounter)
                        }
                    } else if (diffRemoteLocalCounter > 0) {
                        repeat(diffRemoteLocalCounter) {
                            remoteRepo.decrementCounter(localCounter)
                        }
                    }
                } else {
                    val remoteCounterDeleted = remoteRepo.deleteCounter(remoteCounter).getSuccess()
                    if (remoteCounterDeleted != null)
                        localRepo.deleteCounter(remoteCounter)
                }
            }
            Result.Success(localCounters)
        } catch (e: SQLiteConstraintException) {
            Result.Exception(e)
        }
    }
}