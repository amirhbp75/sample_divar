package app.divarinterview.android.data.model.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = PostItemEntity::class,
            parentColumns = ["token"],
            childColumns = ["token"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PostDetailsEntity(
    @PrimaryKey
    val token: String,
    val widgets: String,
    val enableContact: Boolean,
    val contactButtonText: String,
)