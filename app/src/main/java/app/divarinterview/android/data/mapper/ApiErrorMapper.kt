package app.divarinterview.android.data.mapper

import org.json.JSONObject

fun JSONObject.toErrorMessage(): String? {
    if (this.has("message")) {
        val message = this.get("message")

        if (message is String)
            return message
        else if (message is JSONObject)
            return message.optString("title")
    }
    return null
}