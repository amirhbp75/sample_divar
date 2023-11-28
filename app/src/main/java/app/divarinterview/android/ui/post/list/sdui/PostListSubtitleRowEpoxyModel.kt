package app.divarinterview.android.ui.post.list.sdui

import app.divarinterview.android.R
import app.divarinterview.android.databinding.SduiPostListSubtitleBinding
import app.divarinterview.android.utils.ViewBindingKotlinModel
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass

@EpoxyModelClass
abstract class PostListSubtitleRowEpoxyModel :
    ViewBindingKotlinModel<SduiPostListSubtitleBinding>(R.layout.sdui_post_list_subtitle) {

    @EpoxyAttribute
    lateinit var text: String

    override fun SduiPostListSubtitleBinding.bind() {
        postListSubtitleTv.text = text
    }

}