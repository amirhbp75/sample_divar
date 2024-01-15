package app.divarinterview.android.ui.post.list.sdui

import app.divarinterview.android.R
import app.divarinterview.android.databinding.SduiPostListNullBinding
import app.divarinterview.android.utils.ViewBindingKotlinModel
import com.airbnb.epoxy.EpoxyModelClass

@EpoxyModelClass
abstract class PostListEmptyRowEpoxyModel :
    ViewBindingKotlinModel<SduiPostListNullBinding>(R.layout.sdui_post_list_null) {

    override fun SduiPostListNullBinding.bind() {
    }

}