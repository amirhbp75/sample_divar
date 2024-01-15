package app.divarinterview.android.data.source.post

import androidx.room.withTransaction
import app.divarinterview.android.common.container.UserContainer
import app.divarinterview.android.data.database.DivarDatabase
import app.divarinterview.android.data.model.PostDetailsSDUIResponse
import app.divarinterview.android.data.model.PostItemSDUIResponse
import app.divarinterview.android.data.model.local.PostDetailsEntity
import app.divarinterview.android.data.model.local.PostItemEntity
import app.divarinterview.android.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostLocalDataSource @Inject constructor(
    private val database: DivarDatabase
) : PostDataSource {

    /*
    * Post List Actions
    */
    override suspend fun getPostList(
        cityId: Int,
        page: Int,
        last: Long
    ): Flow<Resource<PostItemSDUIResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertPosts(posts: List<PostItemEntity>) =
        database.postItemDao.insertAll(posts)

    override suspend fun deleteAll() =
        database.postItemDao.deleteAll()

    override fun selectPage() =
        database.postItemDao.selectPage()


    /*
    * Post Details Actions
    */
    override suspend fun getPostDetail(token: String): Flow<Resource<PostDetailsSDUIResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertDetails(post: PostDetailsEntity) =
        database.postDetailsDao.insert(post)

    override fun selectDetails(token: String) =
        database.postDetailsDao.select(token)


    /*
    * Utils Functions
    */
    override suspend fun <R> runAsTransaction(block: suspend () -> R) {
        database.withTransaction(block)
    }
}
