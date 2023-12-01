package app.divarinterview.android.ui.post.details.sdui

import android.view.View
import app.divarinterview.android.R
import app.divarinterview.android.databinding.SduiPostDetailsHeaderTitleBinding
import app.divarinterview.android.di.DependencyInjector
import app.divarinterview.android.utils.ViewBindingKotlinModel
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass

@EpoxyModelClass
abstract class PostDetailsHeaderRowEpoxyModel :
    ViewBindingKotlinModel<SduiPostDetailsHeaderTitleBinding>(R.layout.sdui_post_details_header_title) {

    private val imageLoadingService = DependencyInjector.getImageLoadingService()

    @EpoxyAttribute
    lateinit var title: String

    @EpoxyAttribute
    var subtitle: String? = null

    @EpoxyAttribute
    var image: String? = null

    override fun SduiPostDetailsHeaderTitleBinding.bind() {
        rowHeaderTitleTv.text = title
        if (subtitle == null)
            rowHeaderSubtitleTv.visibility = View.GONE
        else
            rowHeaderSubtitleTv.text = subtitle

        image?.let {
            imageLoadingService.load(rowHeaderImageIv, it)
        } ?: kotlin.run {
            rowHeaderImageIv.visibility = View.GONE
        }
    }

}