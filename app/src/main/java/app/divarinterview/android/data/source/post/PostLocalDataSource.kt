package app.divarinterview.android.data.source.post

import app.divarinterview.android.data.model.PostItemSDUIResponse
import app.divarinterview.android.utils.Resource
import kotlinx.coroutines.flow.Flow

class PostLocalDataSource : PostDataSource {
    override suspend fun getPostList(
        cityId: Int,
        page: Int,
        last: Long
    ): Flow<Resource<PostItemSDUIResponse>> {
        TODO("Not yet implemented")
    }
}