package app.divarinterview.android.di

import app.divarinterview.android.common.BASE_URL
import app.divarinterview.android.common.container.UserContainer
import app.divarinterview.android.service.remote.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideApiServiceInstance(): ApiService {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor {
                val oldRequest = it.request()
                val newRequestBuilder = oldRequest.newBuilder()

                newRequestBuilder.addHeader("Content-Type", "application/json")
                newRequestBuilder.addHeader(
                    "x-access-token",
                    "Basic ${UserContainer.token}"
                )

                return@addInterceptor it.proceed(newRequestBuilder.build())
            }.build()


        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }
}