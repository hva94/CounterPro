package com.hvasoft.counterpro.core.common

import androidx.annotation.StringRes
import com.hvasoft.counterpro.R
import retrofit2.HttpException
import java.io.IOException

/**
 * Class to wrap possible errors and handle them properly
 */
sealed class Error {
    object Connectivity : Error()
    class CustomException(@StringRes val messageResId: Int) : Error()
    data class Unknown(val message: String) : Error()
}

/**
 * Convert a exception into a Error validating which exception happened
 * @return Error
 */
fun Exception.toError(): Error = when (this) {
    is IOException -> Error.Connectivity
    is HttpException -> CustomErrors.handleError(code())
    else -> Error.Unknown(message ?: "")
}

/**
 * With a specific code int value, uses handle error function to validate and return a custom exception Error
 * @return Error
 */
fun Int.validateErrorCode(): Error {
    return CustomErrors.handleError(this)
}

object CustomErrors {
    /**
     * Given a code error, validate and returns a custom exception error
     * @param errorCode: value to validate and handle error code
     * @return Error
     */
    fun handleError(errorCode: Int): Error {
        val errorResId = when (errorCode) {
            500 -> R.string.error_empty_title
            501 -> R.string.error_counter_duplicated
            502 -> R.string.error_counter_not_created
            else -> R.string.error_unknown
        }
        return Error.CustomException(messageResId = errorResId)
    }
}