package app.divarinterview.android.data.repository.post

import app.divarinterview.android.data.model.PostDetailsSDUIResponse
import app.divarinterview.android.data.model.PostItemSDUIResponse
import app.divarinterview.android.data.model.local.PostItemEntity
import app.divarinterview.android.utils.Resource
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    suspend fun getPostList(
        cityId: Int,
        page: Int,
        last: Long
    ): Flow<Resource<PostItemSDUIResponse>>

    suspend fun getPostDetail(token: String): Flow<Resource<PostDetailsSDUIResponse>>

    suspend fun insertAll(posts: List<PostItemEntity>)

    suspend fun deleteAll()

    fun selectPage(pagingSize: Int, offset: Int): List<PostItemEntity>

    suspend fun <R> runAsTransaction(block: suspend () -> R)
}