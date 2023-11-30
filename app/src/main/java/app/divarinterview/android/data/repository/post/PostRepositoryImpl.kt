package app.divarinterview.android.data.repository.post

import app.divarinterview.android.data.model.local.PostItemEntity
import app.divarinterview.android.data.source.post.PostDataSource
import javax.inject.Inject
import javax.inject.Named

class PostRepositoryImpl @Inject constructor(
    @Named("postRemote") private val remoteDataSource: PostDataSource,
    @Named("postLocal") private val localDataSource: PostDataSource
) : PostRepository {

    override suspend fun getPostList(
        cityId: Int,
        page: Int,
        last: Long
    ) = remoteDataSource.getPostList(cityId, page, last)

    override suspend fun getPostDetail(token: String) = remoteDataSource.getPostDetail(token)

    override suspend fun insertAll(posts: List<PostItemEntity>) = localDataSource.insertAll(posts)

    override suspend fun deleteAll() = localDataSource.deleteAll()

    override fun selectPage(pagingSize: Int, offset: Int) =
        localDataSource.selectPage(pagingSize, offset)

    override suspend fun <R> runAsTransaction(block: suspend () -> R) =
        localDataSource.runAsTransaction(block)
}