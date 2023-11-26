package app.divarinterview.android.service.remote

import app.divarinterview.android.R
import app.divarinterview.android.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import java.io.IOException

suspend fun <T> callApi(call: suspend () -> Response<T>) = flow {
    emit(Resource.Loading())
    val response = call()

    if (response.isSuccessful) {
        response.body()?.let {
            emit(Resource.Success(it))
        } ?: emit(Resource.Error(R.string.error_fetch_data, null, null))
    } else {
        val errorBodyJson = org.json.JSONObject(response.errorBody()!!.string())
        emit(Resource.Error(R.string.error_fetch_data, response.code(), errorBodyJson))
    }
}.catch { e ->
    if (e is IOException)
        emit(Resource.Error(R.string.error_connection, null, null))
    else
        emit(Resource.Error(R.string.error_fetch_data, null, null))

}.flowOn(Dispatchers.IO)

