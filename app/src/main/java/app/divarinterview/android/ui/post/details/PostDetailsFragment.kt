package app.divarinterview.android.ui.post.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import app.divarinterview.android.common.BaseFragment
import app.divarinterview.android.databinding.FragmentPostDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostDetailsFragment : BaseFragment<FragmentPostDetailsBinding>() {

    private val viewModel: PostDetailsViewModel by viewModels()


    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPostDetailsBinding {
        return FragmentPostDetailsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val postToken = arguments?.getString("token")

        postToken?.let { viewModel.getPostDetail(it) }
        showProgressBar()
    }

    private fun showProgressBar() {
        lifecycleScope.launch {
            viewModel.windowLoadingState.collect {
                setWindowProgressIndicator(it)
            }
        }
    }

}