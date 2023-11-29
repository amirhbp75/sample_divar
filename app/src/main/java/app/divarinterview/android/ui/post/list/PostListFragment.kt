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
import app.divarinterview.android.data.model.PostItemWidgetType
import app.divarinterview.android.databinding.FragmentPostListBinding
import app.divarinterview.android.ui.post.list.sdui.PostListEpoxyController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostListFragment : BaseFragment<FragmentPostListBinding>() {

    private val viewModel: PostListViewModel by viewModels()

    val epoxyController = PostListEpoxyController()
    val loadingItem = PostItemSDUIWidget(PostItemWidgetType.LOADING_ROW, null)
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
        lifecycleScope.launch {
            viewModel.windowLoadingState.collect {
                setFrameProgressIndicator(binding.contentFrame, it)
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
                if (it != null) {
                    postList.takeIf { list -> list.isNotEmpty() && list.last() == loadingItem }
                        ?.removeLastOrNull()

                    postList.addAll(it.widgetList.toMutableList())
                    epoxyController.setData(postList)
                    isLoadingNewPage = false
                    page++
                    lastPostDate = it.lastPostDate
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

                if (!isLoadingNewPage && layoutManager.findLastCompletelyVisibleItemPosition() > totalItemCount - remainToEnd) {
                    isLoadingNewPage = true
                    viewModel.getPostList(page, lastPostDate)
                    postList.add(loadingItem)
                    epoxyController.setData(postList)
                }
            }
        })
    }

    private fun onSwipeRefresh() {
        binding.postListSwipeRefresh.setOnRefreshListener {
            page = 0
            viewModel.getPostList(0, 0)
            binding.postListSwipeRefresh.isRefreshing = false
        }
    }
}