package app.divarinterview.android.data.model

import com.google.gson.annotations.SerializedName

data class PostItemSDUIResponse(
    @SerializedName("widget_list")
    val widgetList: List<PostItemSDUIWidget>,
    @SerializedName("last_post_date")
    val lastPostDate: Long
)
