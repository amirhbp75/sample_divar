package app.divarinterview.android.data.repository.post

import androidx.paging.Pager
import androidx.paging.PagingSource
import app.divarinterview.android.data.model.PostDetailsSDUIResponse
import app.divarinterview.android.data.model.local.PostDetailsEntity
import app.divarinterview.android.data.model.local.PostItemEntity
import app.divarinterview.android.data.paging.PostPagingEvent
import app.divarinterview.android.utils.Resource
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    /*
    * Post List Actions :
    * get from remote
    * insert post list to database
    * delete all post from database
    * select post list base on page from database
    */
    suspend fun getPostList(
        eventCallback: (PostPagingEvent) -> Unit
    ): Pager<Int, PostItemEntity>

    suspend fun insertPosts(posts: List<PostItemEntity>)

    suspend fun deleteAll()

    fun selectPage(): PagingSource<Int, PostItemEntity>


    /*
    * Post Details Actions :
    * get from remote
    * insert post details to database
    * select post details from database
    */
    suspend fun getPostDetail(token: String): Flow<Resource<PostDetailsSDUIResponse>>

    suspend fun insertDetails(post: PostDetailsEntity)

    fun selectDetails(token: String): PostDetailsEntity?


    /*
    * Utils Functions
    */
    suspend fun <R> runAsTransaction(block: suspend () -> R)
}