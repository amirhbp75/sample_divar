package app.divarinterview.android.utils

import androidx.annotation.StringRes

sealed class Resource<T>(
    val data: T? = null,
    val message: Int = 0,
    val throwable: Throwable? = null,
) {
    class Loading<T> : Resource<T>()
    class Success<T>(data: T?) : Resource<T>(data = data)
    class Error<T>(
        @StringRes message: Int,
        throwable: Throwable? = null
    ) : Resource<T>(
        message = message,
        throwable = throwable
    )
}