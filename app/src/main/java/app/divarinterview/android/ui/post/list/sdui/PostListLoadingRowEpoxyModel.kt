package app.divarinterview.android.ui.post.list.sdui

import app.divarinterview.android.R
import app.divarinterview.android.databinding.ItemListMoreLoadingBinding
import app.divarinterview.android.utils.ViewBindingKotlinModel
import com.airbnb.epoxy.EpoxyModelClass

@EpoxyModelClass
abstract class PostListLoadingRowEpoxyModel :
    ViewBindingKotlinModel<ItemListMoreLoadingBinding>(R.layout.item_list_more_loading) {

    override fun ItemListMoreLoadingBinding.bind() {
    }

}