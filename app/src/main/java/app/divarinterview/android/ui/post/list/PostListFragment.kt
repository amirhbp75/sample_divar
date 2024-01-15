package app.divarinterview.android.ui.post.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import app.divarinterview.android.R
import app.divarinterview.android.common.BaseFragment
import app.divarinterview.android.common.container.UserContainer
import app.divarinterview.android.data.model.EmptyState
import app.divarinterview.android.databinding.FragmentPostListBinding
import app.divarinterview.android.ui.post.list.sdui.PostListEpoxyController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch

@OptIn(ObsoleteCoroutinesApi::class)
@AndroidEntryPoint
class PostListFragment : BaseFragment<FragmentPostListBinding>() {

    private val viewModel: PostListViewModel by viewModels()
    private lateinit var epoxyController: PostListEpoxyController

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPostListBinding {
        return FragmentPostListBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backPressHandling()
        showProgressBar()
        showEmptyState()
        selectedCity()
        initList()
        onSwipeRefresh()
    }

    override fun onStart() {
        super.onStart()
        if (UserContainer.token == null || UserContainer.cityId == -1) {
            findNavController().navigate(
                R.id.action_postListFragment_to_welcomeFragment
            )
        }
    }

    private fun backPressHandling() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })
    }

    private fun showProgressBar() {
        dataFlowJob = lifecycleScope.launch {
            viewModel.windowLoadingState.collect {
                setFrameProgressIndicator(binding.contentFrame, it)
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
                            if (state.actionType == EmptyState.ActionType.TRY_AGAIN) {
                                actionBtn.text = getString(R.string.public_try_again)
                                actionBtn.setOnClickListener {
                                    epoxyController.refresh()
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

    private fun selectedCity() {
        binding.selectedCityTv.text =
            getString(R.string.select_city_current_location_result, UserContainer.cityName)

        binding.changeCityTv.setOnClickListener {
            findNavController().navigate(R.id.action_postListFragment_to_selectCityFragment)
        }
    }

    private fun initList() {
        binding.postListRv.itemAnimator = null
        binding.postListRv.setController(epoxyController)
    }

    private fun onSwipeRefresh() {
        binding.postListSwipeRefresh.setOnRefreshListener {
            epoxyController.refresh()
            viewModel.reload = true
            binding.postListSwipeRefresh.isRefreshing = false
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        epoxyController = PostListEpoxyController(onItemClicked = {
            findNavController().navigate(
                R.id.action_postListFragment_to_postDetailFragment,
                Bundle().apply {
                    putString("token", it)
                })
        })

        lifecycleScope.launch {
            viewModel.postListState.collect { paging ->
                paging?.let {
                    epoxyController.submitData(it)
                }
            }
        }
    }
}