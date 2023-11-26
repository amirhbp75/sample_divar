package app.divarinterview.android.common

import androidx.annotation.StringRes

class BaseException(
    val type: Type,
    @StringRes val localMessage: Int = 0,
    val serverMessage: String? = null
) : Throwable() {

    enum class Type {
        AUTHORIZATION, SNACKBAR, TOAST
    }
}