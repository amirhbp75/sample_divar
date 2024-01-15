package app.divarinterview.android.ui.post.list

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import app.divarinterview.android.R
import app.divarinterview.android.common.ApiException
import app.divarinterview.android.common.BaseExceptionMapper
import app.divarinterview.android.common.BaseViewModel
import app.divarinterview.android.data.mapper.toErrorMessage
import app.divarinterview.android.data.mapper.toPostItem
import app.divarinterview.android.data.model.EmptyState
import app.divarinterview.android.data.model.TopAlert
import app.divarinterview.android.data.paging.PostPagingEvent
import app.divarinterview.android.data.repository.post.PostRepository
import app.divarinterview.android.domain.PostItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class PostListViewModel @Inject constructor(
    private val postRepository: PostRepository
) : BaseViewModel() {

    private var _postListState =
        MutableStateFlow<PagingData<PostItem>?>(null)
    val postListState: StateFlow<PagingData<PostItem>?> = _postListState

    var reload: Boolean = false

    init {
        windowLoadingState.value = true
        topAlertState.value = TopAlert(
            mustShow = true,
            loading = true,
            text = R.string.public_update_data
        )
        getPostList()
    }

    private fun getPostList() {
        viewModelScope.launch {
            postRepository.getPostList {
                handelPagingEvent(it)
            }.flow
                .map { pagingData ->
                    pagingData.map {
                        it.toPostItem()
                    }
                }
                .cachedIn(viewModelScope)
                .collect {
                    if (!reload)
                        windowLoadingState.value = false
                    _postListState.value = it
                }
        }
    }

    private fun handelPagingEvent(event: PostPagingEvent) {
        when (event) {
            is PostPagingEvent.Loading -> {
                if (event.page == 0 && reload)
                    windowLoadingState.value = true
            }

            is PostPagingEvent.Success -> {
                topAlertState.value = TopAlert(false)
                windowLoadingState.value = false
                reload = false
            }

            is PostPagingEvent.Error -> {
                event.throwable?.let { error ->
                    if (error is IOException) {
                        topAlertState.value = TopAlert(
                            mustShow = true,
                            text = R.string.public_no_network_connection
                        )
                        windowLoadingState.value = false

                        if (_postListState.value == null)
                            windowEmptyState.value = EmptyState(
                                mustShow = true,
                                actionType = EmptyState.ActionType.TRY_AGAIN,
                                title = R.string.error_connection,
                                subtitleRes = R.string.error_connection_description,
                                actionButton = true
                            )
                    } else if (error is ApiException) {
                        if (error.errorCode == 406) {
                            _postListState.value = null
                            windowLoadingState.value = false
                            windowEmptyState.value = EmptyState(
                                mustShow = true,
                                actionType = EmptyState.ActionType.TRY_AGAIN,
                                title = R.string.public_error_occur,
                                subtitleText = error.errorBody?.toErrorMessage(),
                                actionButton = true
                            )
                        } else
                            EventBus.getDefault().post(
                                BaseExceptionMapper.httpExceptionMapper(
                                    errorCode = 0,
                                    localMessage = event.message,
                                    serverMessage = error.errorBody?.toErrorMessage()
                                )
                            )
                    } else {
                        EventBus.getDefault().post(
                            BaseExceptionMapper.httpExceptionMapper(
                                errorCode = 0,
                                localMessage = event.message
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