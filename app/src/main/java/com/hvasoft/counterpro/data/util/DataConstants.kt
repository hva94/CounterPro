package com.hvasoft.counterpro.data.util

object DataConstants {

    private const val BASE_PATH = "api/v1/"
    const val GET_COUNTERS_PATH = "${BASE_PATH}counters"
    const val BASE_COUNTER_PATH = "${BASE_PATH}counter"
    const val INCREMENT_COUNTER_PATH = "${BASE_COUNTER_PATH}/inc"
    const val DECREMENT_COUNTER_PATH = "${BASE_COUNTER_PATH}/dec"
    const val DELETE_METHOD_NAME = "DELETE"
    const val CONTENT_TYPE_HEADER = "Content-Type: application/json"
    const val TYPE_MEDIA_REQUEST_BODY = "application/json"
    const val PROPERTY_TITLE_REQUEST_BODY = "title"
    const val PROPERTY_ID_REQUEST_BODY = "id"

    const val CONNECT_TIMEOUT = 15L
    const val WRITE_TIMEOUT = 15L
    const val READ_TIMEOUT = 15L

    const val DB_NAME = "counters.db"
    const val TABLE_NAME_COUNTERS = "counters"
    const val INDEX_TITLE_VALUE = "title"

}