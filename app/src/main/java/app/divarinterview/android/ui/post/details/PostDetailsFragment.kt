package app.divarinterview.android.ui.post.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import app.divarinterview.android.R
import app.divarinterview.android.common.BaseFragment
import app.divarinterview.android.data.model.EmptyState
import app.divarinterview.android.databinding.FragmentPostDetailsBinding
import app.divarinterview.android.ui.post.details.sdui.PostDetailsEpoxyController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostDetailsFragment : BaseFragment<FragmentPostDetailsBinding>() {

    private val viewModel: PostDetailsViewModel by viewModels()

    private val epoxyController = PostDetailsEpoxyController()
    var postToken: String? = null
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPostDetailsBinding {
        return FragmentPostDetailsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postToken = arguments?.getString("token")

        postToken?.let { viewModel.getPostDetail(it) }
        showProgressBar()
        showEmptyState()
        initList()
    }

    private fun showProgressBar() {
        lifecycleScope.launch {
            viewModel.windowLoadingState.collect {
                setWindowProgressIndicator(it)
            }
        }

        lifecycleScope.launch {
            viewModel.topAlertState.collect {
                if (it.text != 0)
                    setTopAlert(it.mustShow, it.loading, getString(it.text))
                else
                    setTopAlert(it.mustShow, it.loading, "")
            }
        }
    }

    private fun showEmptyState() {
        lifecycleScope.launch {
            viewModel.windowEmptyState.collect {
                it?.let { state ->
                    if (state.mustShow) {
                        val emptyState = showEmptyState(R.layout.view_window_empty_state)
                        emptyState?.let { view ->
                            val title: TextView = view.findViewById(R.id.emptyStateTitleTv)
                            val subtitle: TextView = view.findViewById(R.id.emptyStateSubtitleTv)
                            val actionBtn: Button = view.findViewById(R.id.emptyStateActionBtn)

                            title.text = getString(state.title)

                            if (state.subtitleText != null)
                                subtitle.text = state.subtitleText
                            else if (state.subtitleRes != 0)
                                subtitle.text = getString(state.subtitleRes)

                            actionBtn.visibility =
                                if (state.actionButton) View.VISIBLE else View.GONE
                            if (state.actionType == EmptyState.ActionType.RETURN) {
                                actionBtn.text = getString(R.string.public_return)
                                actionBtn.setOnClickListener {
                                    findNavController().navigateUp()
                                }
                            } else {
                                actionBtn.text = getString(R.string.public_try_again)
                                actionBtn.setOnClickListener {
                                    postToken?.let { token -> viewModel.getPostDetail(token) }
                                }
                            }
                        }
                    } else {
                        val factory = layoutInflater;
                        val emptyStateView =
                            factory.inflate(R.layout.view_window_empty_state, null);
                        val emptyStateRootView =
                            emptyStateView.findViewById<View>(R.id.emptyStateRootView)
                        emptyStateRootView.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun initList() {
        binding.postDetailsRv.setController(epoxyController)

        lifecycleScope.launch {
            viewModel.postDetailsState.collect {
                it?.let { data ->
                    epoxyController.setData(data.widgetList)
                    if (data.enableContact) {
                        binding.contactBtnLayout.visibility = View.VISIBLE
                        binding.contactBtn.text = data.contactButtonText
                    }
                }
            }
        }
    }

}