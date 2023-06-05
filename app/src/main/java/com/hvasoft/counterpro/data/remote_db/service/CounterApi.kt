package com.hvasoft.counterpro.data.remote_db.service

import com.hvasoft.counterpro.data.remote_db.response.CounterResponseDTO
import com.hvasoft.counterpro.data.util.DataConstants.CONTENT_TYPE_HEADER
import com.hvasoft.counterpro.data.util.DataConstants.GET_COUNTERS_PATH
import com.hvasoft.counterpro.data.util.DataConstants.INSERT_COUNTER_PATH
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface CounterApi {

    @GET(GET_COUNTERS_PATH)
    suspend fun getCounters(): Response<List<CounterResponseDTO.CounterDTO>>

    @Headers(CONTENT_TYPE_HEADER)
    @POST(INSERT_COUNTER_PATH)
    suspend fun insertCounter(@Body requestBody: RequestBody): Response<List<CounterResponseDTO.CounterDTO>>

}