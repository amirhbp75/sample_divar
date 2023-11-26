package app.divarinterview.android.utils

import androidx.annotation.StringRes
import org.json.JSONObject

sealed class Resource<T>(
    val data: T? = null,
    val message: Int? = null,
    val errorCode: Int? = null,
    val errorBodyJson: JSONObject? = null
) {
    class Loading<T> : Resource<T>()
    class Success<T>(data: T?) : Resource<T>(data = data)
    class Error<T>(
        @StringRes message: Int,
        errorCode: Int?,
        errorBodyJson: JSONObject?
    ) : Resource<T>(
        message = message,
        errorCode = errorCode,
        errorBodyJson = errorBodyJson
    )
}