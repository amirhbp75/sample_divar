package app.divarinterview.android.ui.post.list

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.divarinterview.android.R
import app.divarinterview.android.common.BaseFragment
import app.divarinterview.android.common.container.UserContainer
import app.divarinterview.android.data.model.EmptyState
import app.divarinterview.android.data.model.PostItemSDUIWidget
import app.divarinterview.android.databinding.FragmentPostListBinding
import app.divarinterview.android.ui.post.list.sdui.PostListEpoxyController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostListFragment : BaseFragment<FragmentPostListBinding>() {

    private val viewModel: PostListViewModel by viewModels()

    lateinit var epoxyController: PostListEpoxyController
    var postList: MutableList<PostItemSDUIWidget> = mutableListOf()

    private var isLoadingNewPage: Boolean = false
    private var page: Int = 0
    private var lastPostDate: Long = 0

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPostListBinding {
        return FragmentPostListBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        epoxyController = PostListEpoxyController(onItemClicked = {
            findNavController().navigate(
                R.id.action_postListFragment_to_postDetailFragment,
                Bundle().apply {
                    putString("token", it)
                })
        })

        backPressHandling()
        showProgressBar()
        showEmptyState()
        selectedCity()
        initList()
        listScrollListener()
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
                                    viewModel.getDataFromRemote(0, 0, true)
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

        lifecycleScope.launch {
            viewModel.postListState.collect {
                postList.takeIf { list ->
                    list.isNotEmpty() && list.last() == viewModel.loadingItem
                }?.removeLastOrNull()
                isLoadingNewPage = false

                if (it != null) {
                    if ((!viewModel.offlineMode && lastPostDate == 0L) || it.lastPostDate == 0L) {
                        postList = it.widgetList.toMutableList()
                        page = 0
                    } else
                        postList.addAll(it.widgetList.toMutableList())

                    epoxyController.setData(postList)
                    page++
                    if (!viewModel.offlineMode) lastPostDate = it.lastPostDate
                }
            }
        }
    }

    private fun listScrollListener() {
        binding.postListRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val remainToEnd = 5

                if (!viewModel.fetchComplete && !isLoadingNewPage && layoutManager.findLastCompletelyVisibleItemPosition() > totalItemCount - remainToEnd) {
                    isLoadingNewPage = true
                    postList.add(viewModel.loadingItem)
                    epoxyController.setData(postList)
                    viewModel.getDataFromLocal(page, lastPostDate)
                }
            }
        })
    }

    private fun onSwipeRefresh() {
        binding.postListSwipeRefresh.setOnRefreshListener {
            page = 0
            lastPostDate = 0L
            viewModel.getDataFromRemote(0, 0, true)
            binding.postListSwipeRefresh.isRefreshing = false
        }
    }
}