package app.divarinterview.android.data.repository.post

import app.divarinterview.android.data.source.post.PostDataSource
import javax.inject.Inject
import javax.inject.Named

class PostRepositoryImpl @Inject constructor(
    @Named("postRemote") private val remoteDataSource: PostDataSource,
) : PostRepository {

    override suspend fun getPostList(
        cityId: Int,
        page: Int,
        last: Long
    ) = remoteDataSource.getPostList(cityId, page, last)
}