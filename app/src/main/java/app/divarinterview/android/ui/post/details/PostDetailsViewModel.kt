package app.divarinterview.android.ui.post.details

import android.util.Log
import androidx.lifecycle.viewModelScope
import app.divarinterview.android.R
import app.divarinterview.android.common.BaseExceptionMapper
import app.divarinterview.android.common.BaseViewModel
import app.divarinterview.android.data.model.PostDetailsSDUIResponse
import app.divarinterview.android.data.repository.post.PostRepository
import app.divarinterview.android.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

@HiltViewModel
class PostDetailsViewModel @Inject constructor(
    private val postRepository: PostRepository
) : BaseViewModel() {

    private val _postDetailsState= MutableStateFlow<PostDetailsSDUIResponse?>(null)
    val postDetailsState: StateFlow<PostDetailsSDUIResponse?> = _postDetailsState

    fun getPostDetail(token: String) {
        viewModelScope.launch {
            postRepository.getPostDetail(token).collect {
                when (it) {
                    is Resource.Loading -> {
                        windowLoadingState.value = true
                    }

                    is Resource.Success -> {
                        it.data?.let { data ->
                            _postDetailsState.value = data
                            windowLoadingState.value = false
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
                        Log.i("TAG", "getPostDetail: ")
                    }
                }
            }
        }
    }
}