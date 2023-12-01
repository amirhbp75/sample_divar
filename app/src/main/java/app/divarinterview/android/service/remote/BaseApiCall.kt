package app.divarinterview.android.service.remote

import app.divarinterview.android.R
import app.divarinterview.android.common.ApiException
import app.divarinterview.android.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException

suspend fun <T> callApi(call: suspend () -> Response<T>) = flow {
    emit(Resource.Loading())
    val response = call()

    if (response.isSuccessful) {
        response.body()?.let {
            emit(Resource.Success(it))
        } ?: emit(Resource.Error(R.string.error_fetch_data, ApiException()))
    } else {
        val errorBodyJson = JSONObject(response.errorBody()!!.string())
        emit(
            Resource.Error(
                R.string.error_fetch_data,
                ApiException(response.code(), errorBodyJson)
            )
        )
    }
}.catch { e ->
    if (e is IOException)
        emit(Resource.Error(R.string.error_connection, e))
    else
        emit(Resource.Error(R.string.error_fetch_data, e))

}.flowOn(Dispatchers.IO)

