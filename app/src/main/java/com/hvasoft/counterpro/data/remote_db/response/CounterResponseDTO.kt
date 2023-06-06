package com.hvasoft.counterpro.data.remote_db.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CounterResponseDTO(
    @Json(name = "counters")
    val counters: List<CounterDTO>
) {
    @JsonClass(generateAdapter = true)
    data class CounterDTO(
        @Json(name = "count")
        val count: Int,
        @Json(name = "id")
        val id: String,
        @Json(name = "title")
        val title: String
    )
}