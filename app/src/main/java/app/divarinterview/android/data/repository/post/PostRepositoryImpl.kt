package app.divarinterview.android.data.repository.post

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import app.divarinterview.android.data.model.local.PostDetailsEntity
import app.divarinterview.android.data.model.local.PostItemEntity
import app.divarinterview.android.data.paging.PostPagingEvent
import app.divarinterview.android.data.paging.PostRemoteMediator
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
    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getPostList(
        eventCallback: (PostPagingEvent) -> Unit
    ): Pager<Int, PostItemEntity> {

        return Pager(
            config = PagingConfig(
                pageSize = 15,
                prefetchDistance = 4,
                initialLoadSize = 15
            ),
            remoteMediator = PostRemoteMediator(
                remoteDataSource = remoteDataSource,
                localDataSource = localDataSource,
                eventCallback = eventCallback
            ),
            pagingSourceFactory = {
                localDataSource.selectPage()
            }
        )

    }

    override suspend fun insertPosts(posts: List<PostItemEntity>) =
        localDataSource.insertPosts(posts)

    override suspend fun deleteAll() =
        localDataSource.deleteAll()

    override fun selectPage() =
        localDataSource.selectPage()


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