package app.divarinterview.android.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import app.divarinterview.android.data.model.local.PostDetailsEntity
import app.divarinterview.android.data.model.local.PostItemEntity

@Database(
    entities = [PostItemEntity::class, PostDetailsEntity::class],
    version = 1,
    exportSchema = false
)
abstract class DivarDatabase : RoomDatabase() {

    abstract val postItemDao: PostItemDao

    abstract val postDetailsDao: PostDetailsDao
}