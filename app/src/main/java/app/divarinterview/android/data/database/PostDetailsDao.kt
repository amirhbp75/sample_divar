package app.divarinterview.android.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.divarinterview.android.data.model.local.PostDetailsEntity

@Dao
interface PostDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: PostDetailsEntity)

    @Query("SELECT * FROM PostDetailsEntity WHERE token = :token")
    fun select(token: String): PostDetailsEntity?
}