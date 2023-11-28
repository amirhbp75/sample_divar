package app.divarinterview.android.ui.post.list.sdui

import app.divarinterview.android.R
import app.divarinterview.android.databinding.SduiPostListTitleBinding
import app.divarinterview.android.utils.ViewBindingKotlinModel
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass

@EpoxyModelClass
abstract class PostListTitleRowEpoxyModel :
    ViewBindingKotlinModel<SduiPostListTitleBinding>(R.layout.sdui_post_list_title) {

    @EpoxyAttribute
    lateinit var text: String

    override fun SduiPostListTitleBinding.bind() {
        postListTitleTv.text = text
    }

}