package app.divarinterview.android.ui.post.list

import android.util.Log
import androidx.lifecycle.viewModelScope
import app.divarinterview.android.common.BaseViewModel
import app.divarinterview.android.common.container.UserContainer
import app.divarinterview.android.data.model.PostItemSDUIResponse
import app.divarinterview.android.data.repository.post.PostRepository
import app.divarinterview.android.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostListViewModel @Inject constructor(
    private val postRepository: PostRepository
) : BaseViewModel() {

    private var _postListState = MutableStateFlow<PostItemSDUIResponse?>(null)
    val postListState: StateFlow<PostItemSDUIResponse?> = _postListState

    init {
        getPostList()
    }

    fun getPostList() {
        viewModelScope.launch {
            postRepository.getPostList(UserContainer.cityId, 0, 0).collect {
                when (it) {
                    is Resource.Loading -> {
                        windowLoadingState.value = true
                    }

                    is Resource.Success -> {
                        windowLoadingState.value = false
                        it.data?.let { data ->
                            _postListState.value = data
                        }
                    }

                    is Resource.Error -> {
                        windowLoadingState.value = false
                        Log.i("TAGGGG", "getPostList: ")
                    }
                }
            }
        }
    }
}