package com.hvasoft.counterpro.core.common

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

/**
 * Allows to make a request in a safe way catching possible errors
 * @return Result<T>: returns a Result wrapper with the given expected data
 */

suspend fun <T : Any> makeSafeRequest(
    execute: suspend () -> Response<T>
): Result<T> {
    return withContext(Dispatchers.IO) {
        try {
            val response = execute()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Result.Success(body)
            } else {
                Result.Error(code = response.code())
            }
        } catch (e: Exception) {
            Result.Exception(e)
        }
    }
}