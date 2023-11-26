package app.divarinterview.android.ui.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import app.divarinterview.android.R
import app.divarinterview.android.common.BaseFragment
import app.divarinterview.android.databinding.FragmentWelcomeBinding

class WelcomeFragment : BaseFragment<FragmentWelcomeBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentWelcomeBinding {
        return FragmentWelcomeBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginToAppBtn.setOnClickListener {
            findNavController().navigate(
                R.id.action_welcomeFragment_to_selectCityFragment
            )
        }
    }
}