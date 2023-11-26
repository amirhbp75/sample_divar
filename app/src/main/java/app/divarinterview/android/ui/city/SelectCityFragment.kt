package app.divarinterview.android.ui.city

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.divarinterview.android.common.BaseFragment
import app.divarinterview.android.databinding.FragmentSelectCityBinding

class SelectCityFragment : BaseFragment<FragmentSelectCityBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSelectCityBinding {
        return FragmentSelectCityBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}