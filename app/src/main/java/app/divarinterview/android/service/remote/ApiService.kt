package app.divarinterview.android.service.remote

import app.divarinterview.android.data.model.Cities
import app.divarinterview.android.data.model.City
import app.divarinterview.android.data.model.PostItemSDUIResponse
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @GET("place/list")
    suspend fun getCityList(): Response<Cities>

    @POST("place/find")
    suspend fun findCity(
        @Body jsonObject: JsonObject
    ): Response<City>

    @POST("post/list")
    suspend fun getPostList(
        @Query("city") cityId: Int,
        @Body jsonObject: JsonObject
    ): Response<PostItemSDUIResponse>


}