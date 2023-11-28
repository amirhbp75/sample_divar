package app.divarinterview.android.ui.post.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import app.divarinterview.android.R
import app.divarinterview.android.common.BaseFragment
import app.divarinterview.android.common.container.UserContainer
import app.divarinterview.android.databinding.FragmentPostListBinding
import app.divarinterview.android.ui.post.list.sdui.PostListEpoxyController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostListFragment : BaseFragment<FragmentPostListBinding>() {

    private val viewModel: PostListViewModel by viewModels()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPostListBinding {
        return FragmentPostListBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showProgressBar()
        selectedCity()
        initList()

    }

    override fun onStart() {
        super.onStart()
        if (UserContainer.token == null || UserContainer.cityId == -1) {
            findNavController().navigate(
                R.id.action_postListFragment_to_welcomeFragment
            )
        }
    }

    private fun showProgressBar() {
        lifecycleScope.launch {
            viewModel.windowLoadingState.collect {
                setWindowProgressIndicator(it)
            }
        }
    }

    private fun selectedCity() {
        binding.selectedCityTv.text =
            getString(R.string.select_city_current_location_result, UserContainer.cityName)
    }

    private fun initList() {
        val epoxyController = PostListEpoxyController()
        binding.postListRv.setController(epoxyController)

        lifecycleScope.launch {
            viewModel.postListState.collect {
                if (it != null)
                    epoxyController.setData(it.widgetList)
            }
        }
    }
}