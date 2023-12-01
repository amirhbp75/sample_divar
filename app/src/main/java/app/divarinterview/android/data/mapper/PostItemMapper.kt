package app.divarinterview.android.data.mapper

import app.divarinterview.android.data.model.PostItemData
import app.divarinterview.android.data.model.PostItemSDUIWidget
import app.divarinterview.android.data.model.local.PostItemEntity

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

fun PostItemEntity.toPostItemSDUIWidget(): PostItemSDUIWidget {
    return PostItemSDUIWidget(
        widgetType = widgetType,
        data = PostItemData(text, title, token, price, thumbnail, district)
    )
}