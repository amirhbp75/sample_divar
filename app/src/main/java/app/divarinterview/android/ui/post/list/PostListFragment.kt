package app.divarinterview.android.ui.post.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.divarinterview.android.R
import app.divarinterview.android.common.BaseFragment
import app.divarinterview.android.common.container.UserContainer
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
            viewModel.loadingEvent.collect {
                if (postList.isNotEmpty()) {
                    val isFirstItemLoading = postList.first() == viewModel.loadingItem

                    if (it && !isFirstItemLoading) {
                        postList.add(0, viewModel.loadingItem)
                    } else if (!it && isFirstItemLoading) {
                        postList.removeFirst()
                    }

                    epoxyController.setData(postList)
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
                }
                    ?.removeLastOrNull()
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
                    viewModel.fetchData(page, lastPostDate)
                }
            }
        })
    }

    private fun onSwipeRefresh() {
        binding.postListSwipeRefresh.setOnRefreshListener {
            page = 0
            lastPostDate = 0L
            viewModel.getDataRemote(0, 0, true)
            binding.postListSwipeRefresh.isRefreshing = false
        }
    }
}