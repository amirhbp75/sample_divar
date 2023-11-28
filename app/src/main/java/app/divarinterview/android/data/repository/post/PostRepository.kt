package app.divarinterview.android.data.repository.post

import app.divarinterview.android.data.model.PostItemSDUIResponse
import app.divarinterview.android.utils.Resource
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    suspend fun getPostList(cityId: Int, page: Int, last: Long): Flow<Resource<PostItemSDUIResponse>>
}