package app.divarinterview.android.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.divarinterview.android.data.model.local.PostItemEntity

@Dao
interface PostItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<PostItemEntity>)

    @Query("DELETE FROM PostItemEntity")
    suspend fun deleteAll()

    @Query("SELECT * FROM PostItemEntity WHERE cityId = :cityId LIMIT :pagingSize OFFSET :offset")
    fun selectPage(cityId: Int, pagingSize: Int, offset: Int): List<PostItemEntity>
}