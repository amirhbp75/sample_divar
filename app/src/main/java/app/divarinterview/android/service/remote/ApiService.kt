package app.divarinterview.android.service.remote

import app.divarinterview.android.data.model.Cities
import app.divarinterview.android.data.model.City
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("place/list")
    suspend fun getCityList(): Response<Cities>

    @POST("place/find")
    suspend fun findCity(
        @Body jsonObject: JsonObject
    ): Response<City>
}