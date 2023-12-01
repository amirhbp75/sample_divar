package app.divarinterview.android.data.model

import com.google.gson.annotations.SerializedName

data class PostDetailsSDUIWidget(
    @SerializedName("widget_type")
    val widgetType: PostDetailsWidgetType,
    val data: PostDetailsData
)

enum class PostDetailsWidgetType {
    IMAGE_SLIDER_ROW,
    HEADER_ROW,
    INFO_ROW,
    DESCRIPTION_ROW,
    TITLE_ROW
}
