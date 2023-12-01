package app.divarinterview.android.ui.post.details

import androidx.lifecycle.viewModelScope
import app.divarinterview.android.R
import app.divarinterview.android.common.ApiException
import app.divarinterview.android.common.BaseExceptionMapper
import app.divarinterview.android.common.BaseViewModel
import app.divarinterview.android.data.mapper.toErrorMessage
import app.divarinterview.android.data.mapper.toPostDetailsEntity
import app.divarinterview.android.data.model.EmptyState
import app.divarinterview.android.data.model.PostDetailsSDUIResponse
import app.divarinterview.android.data.model.TopAlert
import app.divarinterview.android.data.repository.post.PostRepository
import app.divarinterview.android.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class PostDetailsViewModel @Inject constructor(
    private val postRepository: PostRepository
) : BaseViewModel() {

    private val _postDetailsState = MutableStateFlow<PostDetailsSDUIResponse?>(null)
    val postDetailsState: StateFlow<PostDetailsSDUIResponse?> = _postDetailsState

    fun getPostDetail(token: String) {
        getDataFromLocal(token)
    }

    private fun getDataFromLocal(token: String) {
        windowLoadingState.value = true
        getDataFromRemote(token)
        viewModelScope.launch(Dispatchers.IO) {
            postRepository.selectDetails(token)?.let {
                windowLoadingState.value = false
                _postDetailsState.value = it.toPostDetailsEntity()
                topAlertState.value = TopAlert(
                    mustShow = true,
                    text = R.string.public_update_data
                )
            }
        }
    }

    private fun getDataFromRemote(token: String) {
        viewModelScope.launch {
            postRepository.getPostDetail(token).collect {
                when (it) {
                    is Resource.Loading -> {}

                    is Resource.Success -> {
                        it.data?.let { data ->
                            topAlertState.value = TopAlert(false)
                            windowLoadingState.value = false
                            postRepository.insertDetails(data.toPostDetailsEntity(token))
                            _postDetailsState.value = data
                        } ?: kotlin.run {
                            EventBus.getDefault().post(
                                BaseExceptionMapper.httpExceptionMapper(
                                    0,
                                    R.string.error_fetch_data
                                )
                            )
                        }
                    }

                    is Resource.Error -> {
                        it.throwable?.let { error ->
                            if (error is IOException) {
                                if (_postDetailsState.value == null) {
                                    windowLoadingState.value = false
                                    windowEmptyState.value = EmptyState(
                                        mustShow = true,
                                        actionType = EmptyState.ActionType.TRY_AGAIN,
                                        title = R.string.error_connection,
                                        subtitleRes = R.string.error_connection_description,
                                        actionButton = true
                                    )
                                } else {
                                    topAlertState.value = TopAlert(
                                        mustShow = true,
                                        text = R.string.public_no_network_connection
                                    )
                                }
                            } else if (error is ApiException) {
                                if (error.errorCode == 404) {
                                    windowLoadingState.value = false
                                    windowEmptyState.value = EmptyState(
                                        mustShow = true,
                                        actionType = EmptyState.ActionType.RETURN,
                                        title = R.string.post_details_error_404_not_found,
                                        subtitleText = error.errorBody?.toErrorMessage(),
                                        actionButton = true
                                    )
                                } else
                                    EventBus.getDefault().post(
                                        BaseExceptionMapper.httpExceptionMapper(
                                            errorCode = 0,
                                            localMessage = it.message,
                                            serverMessage = error.errorBody?.toErrorMessage()
                                        )
                                    )
                            } else {
                                EventBus.getDefault().post(
                                    BaseExceptionMapper.httpExceptionMapper(
                                        errorCode = 0,
                                        localMessage = it.message
                                    )
                                )
                            }
                        } ?: kotlin.run {
                            EventBus.getDefault().post(
                                BaseExceptionMapper.httpExceptionMapper(
                                    errorCode = 0,
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}