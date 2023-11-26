package app.divarinterview.android.service.remote

import app.divarinterview.android.data.model.Cities
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("place/list")
    suspend fun getCityList(): Response<Cities>

}