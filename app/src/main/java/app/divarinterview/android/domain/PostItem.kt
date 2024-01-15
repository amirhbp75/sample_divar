package app.divarinterview.android.domain

import app.divarinterview.android.data.model.PostItemWidgetType

data class PostItem(
    val widgetType: PostItemWidgetType,
    val text: String? = null,
    val title: String? = null,
    val token: String? = null,
    val price: String? = null,
    val thumbnail: String? = null,
    val district: String? = null
)
