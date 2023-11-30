package app.divarinterview.android.data.model

import com.google.gson.annotations.SerializedName

data class PostItemSDUIWidget(
    @SerializedName("widget_type")
    val widgetType: PostItemWidgetType,
    val data: PostItemData
)

enum class PostItemWidgetType {
    TITLE_ROW,
    SUBTITLE_ROW,
    POST_ROW,
    LOADING_ROW
}
