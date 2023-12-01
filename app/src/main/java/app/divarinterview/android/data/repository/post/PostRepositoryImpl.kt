package app.divarinterview.android.data.repository.post

import app.divarinterview.android.data.model.local.PostDetailsEntity
import app.divarinterview.android.data.model.local.PostItemEntity
import app.divarinterview.android.data.source.post.PostDataSource
import javax.inject.Inject
import javax.inject.Named

class PostRepositoryImpl @Inject constructor(
    @Named("postRemote") private val remoteDataSource: PostDataSource,
    @Named("postLocal") private val localDataSource: PostDataSource
) : PostRepository {

    /*
    * Post List Actions
    */
    override suspend fun getPostList(
        cityId: Int,
        page: Int,
        last: Long
    ) = remoteDataSource.getPostList(cityId, page, last)

    override suspend fun insertPosts(posts: List<PostItemEntity>) =
        localDataSource.insertPosts(posts)

    override suspend fun deleteAll() =
        localDataSource.deleteAll()

    override fun selectPage(pagingSize: Int, offset: Int) =
        localDataSource.selectPage(pagingSize, offset)


    /*
    * Post Details Actions
    */
    override suspend fun getPostDetail(token: String) =
        remoteDataSource.getPostDetail(token)

    override suspend fun insertDetails(post: PostDetailsEntity) =
        localDataSource.insertDetails(post)

    override fun selectDetails(token: String) =
        localDataSource.selectDetails(token)


    /*
    * Utils Functions
    */
    override suspend fun <R> runAsTransaction(block: suspend () -> R) =
        localDataSource.runAsTransaction(block)
}