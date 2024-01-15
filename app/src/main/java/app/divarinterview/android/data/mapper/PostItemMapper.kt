package app.divarinterview.android.data.mapper

import app.divarinterview.android.data.model.PostItemSDUIWidget
import app.divarinterview.android.data.model.local.PostItemEntity
import app.divarinterview.android.domain.PostItem

fun PostItemSDUIWidget.toPostItemEntity(cityId: Int): PostItemEntity {
    return PostItemEntity(
        cityId = cityId,
        widgetType = widgetType,
        text = data.text,
        title = data.title,
        token = data.token,
        price = data.price,
        thumbnail = data.thumbnail,
        district = data.district
    )
}

fun PostItemEntity.toPostItem(): PostItem {
    return PostItem(
        widgetType = widgetType,
        text = text,
        title = title,
        token = token,
        price = price,
        thumbnail = thumbnail,
        district = district
    )
}