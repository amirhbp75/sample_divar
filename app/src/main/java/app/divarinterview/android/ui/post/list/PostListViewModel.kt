package app.divarinterview.android.ui.post.list

import androidx.lifecycle.viewModelScope
import app.divarinterview.android.R
import app.divarinterview.android.common.BaseExceptionMapper
import app.divarinterview.android.common.BaseViewModel
import app.divarinterview.android.common.container.UserContainer
import app.divarinterview.android.data.mapper.toPostItemEntity
import app.divarinterview.android.data.mapper.toPostItemSDUIWidget
import app.divarinterview.android.data.model.PostItemData
import app.divarinterview.android.data.model.PostItemSDUIResponse
import app.divarinterview.android.data.model.PostItemSDUIWidget
import app.divarinterview.android.data.model.PostItemWidgetType
import app.divarinterview.android.data.model.local.PostItemEntity
import app.divarinterview.android.data.repository.post.PostRepository
import app.divarinterview.android.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
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

    private val _loadingEvent = MutableSharedFlow<Boolean>()
    val loadingEvent = _loadingEvent.asSharedFlow()

    private var pageContent: MutableList<PostItemEntity> = mutableListOf()
    var offlineMode: Boolean = true
    var fetchComplete: Boolean = false
    val loadingItem = PostItemSDUIWidget(PostItemWidgetType.LOADING_ROW, PostItemData())

    init {
        fetchData(0, 0)
    }

    fun fetchData(page: Int, lastDate: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val localData = postRepository.selectPage(PAGE_SIZE, page * PAGE_SIZE)
            pageContent.addAll(localData)

            if (pageContent.isEmpty() || !offlineMode) {
                getDataRemote(page, lastDate)
            } else {
                val postList: MutableList<PostItemSDUIWidget> =
                    localData.map { it.toPostItemSDUIWidget() }.toMutableList()

                fetchComplete = pageContent.isNotEmpty() && localData.isEmpty()
                if (page == 0) {
                    getDataRemote(0, 0)
                    postList.add(0, loadingItem)
                }

                _postListState.value = PostItemSDUIResponse(postList, page.toLong())
            }
        }
    }

    fun getDataRemote(page: Int, lastDate: Long, reload: Boolean = false) {
        viewModelScope.launch {
            postRepository.getPostList(UserContainer.cityId, page, lastDate).collect {
                when (it) {
                    is Resource.Loading -> {
                        if (page == 0 && (_postListState.value == null || reload))
                            windowLoadingState.value = true
                    }

                    is Resource.Success -> {
                        offlineMode = false
                        _loadingEvent.emit(false)
                        windowLoadingState.value = false
                        it.data?.let { data ->
                            val postItemEntity = data.widgetList.map { widgets ->
                                widgets.toPostItemEntity()
                            }

                            postRepository.runAsTransaction {
                                if (page == 0) postRepository.deleteAll()
                                postRepository.insertAll(postItemEntity)
                            }

                            _postListState.value = data
                        } ?: kotlin.run {
                            _postListState.value = null
                            EventBus.getDefault().post(
                                BaseExceptionMapper.httpExceptionMapper(
                                    0,
                                    R.string.error_fetch_data
                                )
                            )
                        }
                    }

                    is Resource.Error -> {
                        if (it.errorCode == 999) {
                            _loadingEvent.emit(false)
                            if (offlineMode && pageContent.isNotEmpty())
                                windowLoadingState.value = false
                        }

                        _postListState.value = null
                        var message: String? = null
                        it.errorBodyJson?.let { errorBody ->
                            if (errorBody.has("message")) {
                                message = errorBody.getString("message")
                            }
                        }

                        EventBus.getDefault().post(
                            BaseExceptionMapper.httpExceptionMapper(
                                errorCode = it.errorCode,
                                localMessage = it.message,
                                serverMessage = message
                            )
                        )
                    }
                }
            }
        }
    }
}