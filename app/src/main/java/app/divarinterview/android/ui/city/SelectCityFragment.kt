package app.divarinterview.android.ui.city

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.divarinterview.android.common.BaseFragment
import app.divarinterview.android.databinding.FragmentSelectCityBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SelectCityFragment @Inject constructor(
    private val cityListAdapter: CityListAdapter
) : BaseFragment<FragmentSelectCityBinding>() {

    private val viewModel: SelectCityViewModel by viewModels()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSelectCityBinding {
        return FragmentSelectCityBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showProgressBar()
        initAdapter()
        fetchData()
    }

    private fun showProgressBar() {
        lifecycleScope.launch {
            viewModel.windowLoadingState.collect {
                setWindowProgressIndicator(it)
            }
        }
    }

    private fun initAdapter() {
        binding.cityListRv.apply {
            adapter = cityListAdapter
            layoutManager = LinearLayoutManager(
                requireContext(),
                RecyclerView.VERTICAL,
                false
            )
        }

        cityListAdapter.setOnItemClickListener {
            Toast.makeText(requireContext(), "$it", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchData() {
        lifecycleScope.launch {
            viewModel.cityListState.collect {
                cityListAdapter.submitList(it)
            }
        }
    }

}