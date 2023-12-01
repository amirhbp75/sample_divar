package app.divarinterview.android.data.mapper

import app.divarinterview.android.data.model.PostDetailsSDUIResponse
import app.divarinterview.android.data.model.PostDetailsSDUIWidget
import app.divarinterview.android.data.model.local.PostDetailsEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun PostDetailsSDUIResponse.toPostDetailsEntity(token: String): PostDetailsEntity {
    return PostDetailsEntity(
        token = token,
        widgets = Gson().toJson(widgetList),
        enableContact = enableContact,
        contactButtonText = contactButtonText
    )
}

fun PostDetailsEntity.toPostDetailsEntity(): PostDetailsSDUIResponse {
    val listType = object : TypeToken<List<PostDetailsSDUIWidget>>() {}.type

    return PostDetailsSDUIResponse(
        widgetList = Gson().fromJson(widgets, listType),
        enableContact = enableContact,
        contactButtonText = contactButtonText
    )
}