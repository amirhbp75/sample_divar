package app.divarinterview.android.ui.post.details.sdui

import app.divarinterview.android.R
import app.divarinterview.android.databinding.SduiPostDetailsTextRowBinding
import app.divarinterview.android.utils.ViewBindingKotlinModel
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass

@EpoxyModelClass
abstract class PostDetailsTextRowEpoxyModel :
    ViewBindingKotlinModel<SduiPostDetailsTextRowBinding>(R.layout.sdui_post_details_text_row) {

    @EpoxyAttribute
    lateinit var text: String

    override fun SduiPostDetailsTextRowBinding.bind() {
        rowTextDetailsTv.text = text
    }

}