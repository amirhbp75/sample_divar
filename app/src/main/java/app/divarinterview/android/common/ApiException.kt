package app.divarinterview.android.common

import org.json.JSONObject

class ApiException(
    val errorCode: Int = 0,
    val errorBody: JSONObject? = null
) : Throwable()