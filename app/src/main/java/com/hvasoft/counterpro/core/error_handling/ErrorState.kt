package com.hvasoft.counterpro.core.error_handling

import androidx.annotation.StringRes

data class ErrorState(
    @StringRes val errorMessageRes: Int
)