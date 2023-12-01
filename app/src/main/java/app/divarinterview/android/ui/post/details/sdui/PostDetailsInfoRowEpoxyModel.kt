package app.divarinterview.android.ui.post.details.sdui

import app.divarinterview.android.R
import app.divarinterview.android.databinding.SduiPostDetailsInfoRowBinding
import app.divarinterview.android.utils.ViewBindingKotlinModel
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass

@EpoxyModelClass
abstract class PostDetailsInfoRowEpoxyModel :
    ViewBindingKotlinModel<SduiPostDetailsInfoRowBinding>(R.layout.sdui_post_details_info_row) {

    @EpoxyAttribute
    lateinit var title: String

    @EpoxyAttribute
    lateinit var subtitle: String

    override fun SduiPostDetailsInfoRowBinding.bind() {
        infoTitleTv.text = title
        infoSubtitleTv.text = subtitle
    }
}