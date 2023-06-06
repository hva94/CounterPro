package com.hvasoft.counterpro.data.repository

import com.google.gson.JsonObject
import com.hvasoft.counterpro.core.common.Result
import com.hvasoft.counterpro.core.common.fold
import com.hvasoft.counterpro.core.common.makeSafeRequest
import com.hvasoft.counterpro.core.util.toDomain
import com.hvasoft.counterpro.data.remote_db.service.CounterApi
import com.hvasoft.counterpro.data.util.DataConstants.PROPERTY_ID_REQUEST_BODY
import com.hvasoft.counterpro.data.util.DataConstants.PROPERTY_TITLE_REQUEST_BODY
import com.hvasoft.counterpro.data.util.DataConstants.TYPE_MEDIA_REQUEST_BODY
import com.hvasoft.counterpro.domain.model.Counter
import com.hvasoft.counterpro.domain.repository.CounterRemoteRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class CounterRemoteRepositoryImpl @Inject constructor(
    private val counterApi: CounterApi
) : CounterRemoteRepository {

    override suspend fun getCounters(): Result<List<Counter>> {
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

    override suspend fun insertCounter(title: String): Result<List<Counter>>  {
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

    override suspend fun incrementCounter(counter: Counter): Result<List<Counter>> {
        val jsonObject = JsonObject()
        jsonObject.addProperty(PROPERTY_ID_REQUEST_BODY, counter.id)
        val requestBody = jsonObject.toString().toRequestBody(TYPE_MEDIA_REQUEST_BODY.toMediaTypeOrNull())
        val response = makeSafeRequest { counterApi.incrementCounter(requestBody) }
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

    override suspend fun decrementCounter(counter: Counter): Result<List<Counter>> {
        val jsonObject = JsonObject()
        jsonObject.addProperty(PROPERTY_ID_REQUEST_BODY, counter.id)
        val requestBody = jsonObject.toString().toRequestBody(TYPE_MEDIA_REQUEST_BODY.toMediaTypeOrNull())
        val response = makeSafeRequest { counterApi.decrementCounter(requestBody) }
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