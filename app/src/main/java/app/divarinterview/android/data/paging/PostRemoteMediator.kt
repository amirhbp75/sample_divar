package app.divarinterview.android.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import app.divarinterview.android.R
import app.divarinterview.android.common.container.UserContainer
import app.divarinterview.android.data.mapper.toPostItemEntity
import app.divarinterview.android.data.model.PostItemSDUIWidget
import app.divarinterview.android.data.model.local.PostItemEntity
import app.divarinterview.android.data.source.post.PostDataSource
import app.divarinterview.android.utils.Resource
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PostRemoteMediator(
    private val remoteDataSource: PostDataSource,
    private val localDataSource: PostDataSource,
    private val eventCallback: (PostPagingEvent) -> Unit
) : RemoteMediator<Int, PostItemEntity>() {

    private var currentPage = 0
    private var lastPostDate = 0L

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostItemEntity>
    ): MediatorResult {
        try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    currentPage = 0
                    lastPostDate = 0L
                    0
                }

                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )

                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        currentPage = 0
                        lastPostDate = 0L
                        0
                    } else {
                        currentPage += 1
                        currentPage
                    }
                }
            }
            var throwable: Throwable? = null
            var result: List<PostItemSDUIWidget> = emptyList()
            val response = remoteDataSource.getPostList(UserContainer.cityId, page, lastPostDate)

            response.collect {
                when (it) {
                    is Resource.Loading -> {
                        eventCallback(PostPagingEvent.Loading(page))
                    }

                    is Resource.Error -> {
                        eventCallback(PostPagingEvent.Error(it.message, it.throwable))
                        throwable = it.throwable
                        currentPage -= 1
                    }

                    is Resource.Success -> {
                        eventCallback(PostPagingEvent.Success)
                        lastPostDate = it.data?.lastPostDate!!
                        result = it.data.widgetList
                    }
                }
            }

            return if (throwable != null)
                MediatorResult.Error(throwable!!)
            else {
                localDataSource.runAsTransaction {
                    if (loadType == LoadType.REFRESH) {
                        localDataSource.deleteAll()
                    }
                    val postItemEntity = result.map { it.toPostItemEntity(1) }
                    localDataSource.insertPosts(postItemEntity)
                }
                MediatorResult.Success(
                    endOfPaginationReached = result.isEmpty()
                )
            }
        } catch (e: IOException) {
            eventCallback(PostPagingEvent.Error(R.string.error_connection, e))
            return MediatorResult.Error(e)
        } catch (e: Exception) {
            eventCallback(PostPagingEvent.Error(R.string.error_fetch_data, e))
            return MediatorResult.Error(e)
        }
    }
}