package com.hvasoft.counterpro.domain.use_case

import android.database.sqlite.SQLiteConstraintException
import com.hvasoft.counterpro.core.common.Result
import com.hvasoft.counterpro.core.common.getSuccess
import com.hvasoft.counterpro.domain.model.Counter
import com.hvasoft.counterpro.domain.repository.CounterLocalRepository
import com.hvasoft.counterpro.domain.repository.CounterRemoteRepository
import javax.inject.Inject
import kotlin.math.abs

class IncrementCounterUC @Inject constructor(
    private val localRepo: CounterLocalRepository,
    private val remoteRepo: CounterRemoteRepository,
) {
    suspend operator fun invoke(counter: Counter): Result<List<Counter>> {
        counter.count++
        try {
            localRepo.insertCounter(counter)
        } catch (e: SQLiteConstraintException) {
            return Result.Exception(e)
        }
        remoteRepo.incrementCounter(counter)
        return try {
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
    }
}