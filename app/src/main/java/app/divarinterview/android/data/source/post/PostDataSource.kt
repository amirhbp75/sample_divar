package app.divarinterview.android.data.source.post

import app.divarinterview.android.data.model.PostItemSDUIResponse
import app.divarinterview.android.utils.Resource
import kotlinx.coroutines.flow.Flow

interface PostDataSource {
    suspend fun getPostList(cityId: Int, page: Int, last: Long): Flow<Resource<PostItemSDUIResponse>>

}