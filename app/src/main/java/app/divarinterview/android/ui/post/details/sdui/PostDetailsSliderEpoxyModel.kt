package app.divarinterview.android.ui.post.details.sdui

import androidx.viewpager2.widget.ViewPager2
import app.divarinterview.android.R
import app.divarinterview.android.data.model.PostDetailsSliderItem
import app.divarinterview.android.databinding.SduiPostDetailsSliderBinding
import app.divarinterview.android.di.DependencyInjector
import app.divarinterview.android.ui.post.details.PostDetailsSliderAdapter
import app.divarinterview.android.utils.ViewBindingKotlinModel
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass

@EpoxyModelClass
abstract class PostDetailsSliderEpoxyModel :
    ViewBindingKotlinModel<SduiPostDetailsSliderBinding>(R.layout.sdui_post_details_slider) {

    private val imageLoadingService = DependencyInjector.getImageLoadingService()

    private var selected: Int = 0

    @EpoxyAttribute
    lateinit var slides: List<PostDetailsSliderItem>

    override fun SduiPostDetailsSliderBinding.bind() {
        val sliderAdapter = PostDetailsSliderAdapter(slides, imageLoadingService)
        sliderViewPager.adapter = sliderAdapter
        sliderViewPager.offscreenPageLimit = 3
        sliderIndicator.attachTo(sliderViewPager)

        sliderViewPager.setCurrentItem(selected, false)

        sliderViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                selected = position
            }
        })


    }
}