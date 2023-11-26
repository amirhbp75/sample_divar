package app.divarinterview.android.utils

import androidx.annotation.StringRes
import org.json.JSONObject

sealed class Resource<T>(
    val data: T? = null,
    val message: Int = 0,
    val errorCode: Int = 0,
    val errorBodyJson: JSONObject? = null
) {
    class Loading<T> : Resource<T>()
    class Success<T>(data: T?) : Resource<T>(data = data)
    class Error<T>(
        @StringRes message: Int,
        errorCode: Int = 0,
        errorBodyJson: JSONObject? = null
    ) : Resource<T>(
        message = message,
        errorCode = errorCode,
        errorBodyJson = errorBodyJson
    )
}