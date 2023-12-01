package app.divarinterview.android.data.source.post

import app.divarinterview.android.data.model.local.PostDetailsEntity
import app.divarinterview.android.data.model.local.PostItemEntity
import app.divarinterview.android.service.remote.ApiService
import app.divarinterview.android.service.remote.callApi
import com.google.gson.JsonObject
import javax.inject.Inject

class PostRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) : PostDataSource {

    /*
    * Post List Actions
    */
    override suspend fun getPostList(
        cityId: Int,
        page: Int,
        last: Long
    ) = callApi {
        apiService.getPostList(cityId, JsonObject().apply {
            addProperty("page", page)
            addProperty("last_post_date", last)
        })
    }

    override suspend fun insertPosts(posts: List<PostItemEntity>) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAll() {
        TODO("Not yet implemented")
    }

    override fun selectPage(pagingSize: Int, offset: Int): List<PostItemEntity> {
        TODO("Not yet implemented")
    }


    /*
    * Post Details Actions
    */
    override suspend fun getPostDetail(token: String) = callApi {
        apiService.getPostDetails(token)
    }

    override suspend fun insertDetails(post: PostDetailsEntity) {
        TODO("Not yet implemented")
    }

    override fun selectDetails(token: String): PostDetailsEntity? {
        TODO("Not yet implemented")
    }


    /*
    * Utils Functions
    */
    override suspend fun <R> runAsTransaction(block: suspend () -> R) {
        TODO("Not yet implemented")
    }
}