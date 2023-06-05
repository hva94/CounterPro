package com.hvasoft.counterpro.core.error_handling

import com.hvasoft.counterpro.R
import com.hvasoft.counterpro.core.common.Error

object ErrorResolver {

    fun handleError(error: Error): ErrorState {
        return when (error) {
            Error.Connectivity -> ErrorState(
                errorMessageRes = R.string.error_connectivity
            )

            is Error.CustomException -> ErrorState(
                errorMessageRes = error.messageResId
            )

            is Error.Unknown -> ErrorState(
                errorMessageRes = R.string.error_unknown
            )
        }
    }

}