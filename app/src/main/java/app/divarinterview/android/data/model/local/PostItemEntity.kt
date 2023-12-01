package app.divarinterview.android.data.model.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import app.divarinterview.android.data.model.PostItemWidgetType

@Entity(indices = [Index(value = ["token"], unique = true)])
data class PostItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val cityId: Int,
    val widgetType: PostItemWidgetType,
    val text: String?,
    val title: String?,
    val token: String?,
    val price: String?,
    val thumbnail: String?,
    val district: String?,
)