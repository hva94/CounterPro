package com.hvasoft.counterpro.data.repository

import com.google.gson.JsonObject
import com.hvasoft.counterpro.core.common.Result
import com.hvasoft.counterpro.core.common.fold
import com.hvasoft.counterpro.core.common.makeSafeRequest
import com.hvasoft.counterpro.core.util.toDomain
import com.hvasoft.counterpro.core.util.toEntity
import com.hvasoft.counterpro.data.local_db.dao.CounterDao
import com.hvasoft.counterpro.data.remote_db.service.CounterApi
import com.hvasoft.counterpro.data.util.DataConstants.TYPE_MEDIA_REQUEST_BODY
import com.hvasoft.counterpro.data.util.DataConstants.PROPERTY_TITLE_REQUEST_BODY
import com.hvasoft.counterpro.domain.model.Counter
import com.hvasoft.counterpro.domain.repository.CounterRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class CounterRepositoryImpl @Inject constructor(
    private val counterApi: CounterApi,
    private val counterDao: CounterDao
) : CounterRepository {

    override suspend fun getCountersRemote(): Result<List<Counter>> {
        val response = makeSafeRequest { counterApi.getCounters() }
        return response.fold(
            onSuccess = { counters ->
                Result.Success(counters.map { counter -> counter.toDomain() })
            },
            onError = { code ->
                Result.Error(code)
            },
            onException = {
                Result.Exception(it)
            }
        )
    }

    override suspend fun getCountersLocal(): List<Counter> {
        return counterDao.getCounters().map { counter -> counter.toDomain() }
    }

    override suspend fun getCounterByTitle(title: String): Counter? {
        return counterDao.getCounterByTitle(title)?.toDomain()
    }

    override suspend fun insertCounterLocal(counter: Counter) {
        counterDao.insertCounter(counter.toEntity())
    }

    override suspend fun insertCounterRemote(title: String): Result<List<Counter>>  {
        val jsonObject = JsonObject()
        jsonObject.addProperty(PROPERTY_TITLE_REQUEST_BODY, title)
        val requestBody = jsonObject.toString().toRequestBody(TYPE_MEDIA_REQUEST_BODY.toMediaTypeOrNull())
        val response = makeSafeRequest { counterApi.insertCounter(requestBody) }
        return response.fold(
            onSuccess = { counters ->
                Result.Success(counters.map { counter -> counter.toDomain() })
            },
            onError = { code ->
                Result.Error(code)
            },
            onException = {
                Result.Exception(it)
            }
        )
    }
}