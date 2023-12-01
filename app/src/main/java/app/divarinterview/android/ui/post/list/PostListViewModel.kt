package app.divarinterview.android.ui.post.list

import androidx.lifecycle.viewModelScope
import app.divarinterview.android.R
import app.divarinterview.android.common.ApiException
import app.divarinterview.android.common.BaseExceptionMapper
import app.divarinterview.android.common.BaseViewModel
import app.divarinterview.android.common.container.UserContainer
import app.divarinterview.android.data.mapper.toErrorMessage
import app.divarinterview.android.data.mapper.toPostItemEntity
import app.divarinterview.android.data.mapper.toPostItemSDUIWidget
import app.divarinterview.android.data.model.EmptyState
import app.divarinterview.android.data.model.PostItemData
import app.divarinterview.android.data.model.PostItemSDUIResponse
import app.divarinterview.android.data.model.PostItemSDUIWidget
import app.divarinterview.android.data.model.PostItemWidgetType
import app.divarinterview.android.data.model.TopAlert
import app.divarinterview.android.data.model.local.PostItemEntity
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
class PostListViewModel @Inject constructor(
    private val postRepository: PostRepository
) : BaseViewModel() {

    companion object {
        private const val PAGE_SIZE = 24
    }

    private var _postListState = MutableStateFlow<PostItemSDUIResponse?>(null)
    val postListState: StateFlow<PostItemSDUIResponse?> = _postListState

    private var pageContent: MutableList<PostItemEntity> = mutableListOf()
    var offlineMode: Boolean = true
    var fetchComplete: Boolean = false
    val loadingItem = PostItemSDUIWidget(PostItemWidgetType.LOADING_ROW, PostItemData())

    init {
        getDataFromLocal(0, 0)
    }

    fun getDataFromLocal(page: Int, lastDate: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val localData = postRepository.selectPage(PAGE_SIZE, page * PAGE_SIZE)
            pageContent.addAll(localData)

            if (pageContent.isEmpty() || !offlineMode) {
                getDataFromRemote(page, lastDate)
            } else {
                val postList: MutableList<PostItemSDUIWidget> =
                    localData.map { it.toPostItemSDUIWidget() }.toMutableList()

                fetchComplete = pageContent.isNotEmpty() && localData.isEmpty()
                if (page == 0) {
                    getDataFromRemote(0, 0)
                    topAlertState.value = TopAlert(
                        mustShow = true,
                        loading = true,
                        text = R.string.public_update_data
                    )
                }

                _postListState.value = PostItemSDUIResponse(postList, page.toLong())
            }
        }
    }

    fun getDataFromRemote(page: Int, lastDate: Long, reload: Boolean = false) {
        viewModelScope.launch {
            postRepository.getPostList(UserContainer.cityId, page, lastDate).collect {
                when (it) {
                    is Resource.Loading -> {
                        if (page == 0 && (_postListState.value == null || reload))
                            windowLoadingState.value = true
                    }

                    is Resource.Success -> {
                        topAlertState.value = TopAlert(false)
                        windowLoadingState.value = false
                        it.data?.let { data ->
                            val postItemEntity = data.widgetList.map { widgets ->
                                widgets.toPostItemEntity(UserContainer.cityId)
                            }

                            postRepository.runAsTransaction {
                                if (page == 0) postRepository.deleteAll()
                                postRepository.insertPosts(postItemEntity)
                            }

                            fetchComplete = false
                            offlineMode = false
                            _postListState.value = data
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
                                topAlertState.value = TopAlert(
                                    mustShow = true,
                                    text = R.string.public_no_network_connection
                                )
                                windowLoadingState.value = false

                                if (!offlineMode && pageContent.isEmpty() && _postListState.value == null)
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