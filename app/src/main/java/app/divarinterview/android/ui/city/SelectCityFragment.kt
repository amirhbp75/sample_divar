package app.divarinterview.android.ui.city

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.divarinterview.android.R
import app.divarinterview.android.common.BaseFragment
import app.divarinterview.android.databinding.FragmentSelectCityBinding
import app.divarinterview.android.utils.checkGpsEnable
import app.divarinterview.android.utils.checkLocationPermission
import app.divarinterview.android.utils.requestForEnableGps
import app.divarinterview.android.utils.requestForLocationPermission
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SelectCityFragment @Inject constructor(
    private val cityListAdapter: CityListAdapter
) : BaseFragment<FragmentSelectCityBinding>() {

    private val viewModel: SelectCityViewModel by viewModels()

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 765
    }

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
        searchCityEditTextListener()
        showUserCurrentCity()
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
            viewModel.selectCity(it)
            findNavController().navigate(
                R.id.action_selectCityFragment_to_postListFragment
            )
        }
    }

    private fun fetchData() {
        lifecycleScope.launch {
            viewModel.cityListState.collect {
                cityListAdapter.submitList(it) {
                    binding.cityListRv.scrollToPosition(0)
                }
            }
        }
    }

    private fun searchCityEditTextListener() {
        binding.searchCityNameEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {
                viewModel.searchInList(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun showUserCurrentCity() {
        if (checkLocationPermission(requireContext()) && checkGpsEnable(requireContext())) {
            viewModel.getUserCity()
        }

        binding.findMyCurrentCityTv.setOnClickListener {
            if (viewModel.userCurrentCity.value != null) {
                viewModel.selectCity(viewModel.userCurrentCity.value!!)
                findNavController().navigate(
                    R.id.action_selectCityFragment_to_postListFragment
                )
            } else {
                if (!checkLocationPermission(requireContext()))
                    requestForLocationPermission(
                        requireActivity(),
                        LOCATION_PERMISSION_REQUEST_CODE
                    )
                else if (!checkGpsEnable(requireContext()))
                    requestForEnableGps(requireContext())
                else {
                    viewModel.getUserCity()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.userCurrentCity.collect {
                it?.let { city ->
                    if (city.id == -1) {
                        binding.findMyCurrentCityTv.isEnabled = false
                        binding.findMyCurrentCityTv.text =
                            getString(R.string.select_city_current_location_loading)
                    } else {
                        binding.findMyCurrentCityTv.isEnabled = true
                        binding.findMyCurrentCityTv.text =
                            getString(R.string.select_city_current_location_result, city.name)
                    }
                } ?: kotlin.run {
                    binding.findMyCurrentCityTv.isEnabled = true
                    binding.findMyCurrentCityTv.text =
                        getString(R.string.select_city_current_location)
                }
            }
        }
    }
}