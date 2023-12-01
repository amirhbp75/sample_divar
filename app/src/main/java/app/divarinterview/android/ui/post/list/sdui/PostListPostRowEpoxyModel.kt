package app.divarinterview.android.ui.post.list.sdui

import android.view.View
import app.divarinterview.android.R
import app.divarinterview.android.data.model.PostItemData
import app.divarinterview.android.databinding.SduiPostListCardBinding
import app.divarinterview.android.di.DependencyInjector
import app.divarinterview.android.utils.ViewBindingKotlinModel
import app.divarinterview.android.utils.clickButtonAnimation
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass

@EpoxyModelClass
abstract class PostListPostRowEpoxyModel :
    ViewBindingKotlinModel<SduiPostListCardBinding>(R.layout.sdui_post_list_card) {

    private val imageLoadingService = DependencyInjector.getImageLoadingService()

    @EpoxyAttribute
    lateinit var data: PostItemData

    @EpoxyAttribute
    lateinit var onClick: (String) -> Unit

    override fun SduiPostListCardBinding.bind() {
        data.thumbnail?.let {
            imageLoadingService.load(
                postItemThumbnailIv,
                it
            )
        }
        postItemTitleTv.text = data.title
        postItemPriceTv.text = data.price
        if (data.district == "")
            postItemDistrictTv.visibility = View.GONE
        else postItemDistrictTv.text = data.district

        root.clickButtonAnimation()
        root.setOnClickListener { data.token?.let { token -> onClick(token) } }
    }

}