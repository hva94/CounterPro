package com.hvasoft.counterpro.data.remote_db.service

import com.hvasoft.counterpro.data.remote_db.response.CounterResponseDTO
import com.hvasoft.counterpro.data.util.DataConstants.BASE_COUNTER_PATH
import com.hvasoft.counterpro.data.util.DataConstants.CONTENT_TYPE_HEADER
import com.hvasoft.counterpro.data.util.DataConstants.DECREMENT_COUNTER_PATH
import com.hvasoft.counterpro.data.util.DataConstants.DELETE_METHOD_NAME
import com.hvasoft.counterpro.data.util.DataConstants.GET_COUNTERS_PATH
import com.hvasoft.counterpro.data.util.DataConstants.INCREMENT_COUNTER_PATH
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Headers
import retrofit2.http.POST

interface CounterApi {

    @GET(GET_COUNTERS_PATH)
    suspend fun getCounters(): Response<List<CounterResponseDTO.CounterDTO>>

    @Headers(CONTENT_TYPE_HEADER)
    @POST(BASE_COUNTER_PATH)
    suspend fun insertCounter(@Body requestBody: RequestBody): Response<List<CounterResponseDTO.CounterDTO>>

    @Headers(CONTENT_TYPE_HEADER)
    @POST(INCREMENT_COUNTER_PATH)
    suspend fun incrementCounter(@Body requestBody: RequestBody): Response<List<CounterResponseDTO.CounterDTO>>

    @Headers(CONTENT_TYPE_HEADER)
    @POST(DECREMENT_COUNTER_PATH)
    suspend fun decrementCounter(@Body requestBody: RequestBody): Response<List<CounterResponseDTO.CounterDTO>>

    @HTTP(method = DELETE_METHOD_NAME, path = BASE_COUNTER_PATH, hasBody = true)
    suspend fun deleteCounter(@Body requestBody: RequestBody): Response<List<CounterResponseDTO.CounterDTO>>

}