package app.divarinterview.android.data.model

import com.google.gson.annotations.SerializedName

data class PostItemSDUIWidget(
    @SerializedName("widget_type")
    val widgetType: String,
    val text: String?,
    val data: PostItemData?
)
