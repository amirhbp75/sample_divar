package app.divarinterview.android.ui.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import app.divarinterview.android.ui.city.CityListAdapter
import app.divarinterview.android.ui.city.SelectCityFragment
import javax.inject.Inject

class BaseFragmentFactory @Inject constructor(
    private val cityListAdapter: CityListAdapter
) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            SelectCityFragment::class.java.name -> SelectCityFragment(cityListAdapter)
            else -> super.instantiate(classLoader, className)
        }
    }
}