package app.divarinterview.android.ui.post.details

import android.util.Log
import androidx.lifecycle.viewModelScope
import app.divarinterview.android.common.BaseViewModel
import app.divarinterview.android.data.repository.post.PostRepository
import app.divarinterview.android.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailsViewModel @Inject constructor(
    private val postRepository: PostRepository
) : BaseViewModel() {

    fun getPostDetail(token: String) {
        viewModelScope.launch {
            postRepository.getPostDetail(token).collect {
                when (it) {
                    is Resource.Loading -> {
                        windowLoadingState.value = true
                    }

                    is Resource.Success -> {
                        Log.i("TAG", "getPostDetail: ")
                    }

                    is Resource.Error -> {
                        Log.i("TAG", "getPostDetail: ")
                    }
                }
            }
        }
    }
}