package app.divarinterview.android.ui.post.list.sdui

import app.divarinterview.android.data.model.PostItemWidgetType
import app.divarinterview.android.domain.PostItem
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging3.PagingDataEpoxyController
import kotlinx.coroutines.ObsoleteCoroutinesApi

@ObsoleteCoroutinesApi
class PostListEpoxyController(
    private val onItemClicked: (String) -> Unit
) : PagingDataEpoxyController<PostItem>() {

    override fun buildItemModel(currentPosition: Int, item: PostItem?): EpoxyModel<*> {
        return when (item?.widgetType) {
            PostItemWidgetType.TITLE_ROW -> {
                item.text?.let {
                    PostListTitleRowEpoxyModel_()
                        .id("titleRow")
                        .text(it)
                } ?: run {
                    error("Unknown EpisodesUiModel type")
                }
            }

            PostItemWidgetType.SUBTITLE_ROW -> {
                item.text?.let {
                    PostListSubtitleRowEpoxyModel_()
                        .id("subtitleRow")
                        .text(it)
                } ?: run {
                    error("Unknown EpisodesUiModel type")
                }
            }

            PostItemWidgetType.POST_ROW -> {
                item.let {
                    PostListPostRowEpoxyModel_()
                        .id(it.token)
                        .data(it)
                        .onClick(onItemClicked)
                } ?: run {
                    error("Unknown EpisodesUiModel type")
                }
            }

            PostItemWidgetType.LOADING_ROW -> {
                PostListLoadingRowEpoxyModel_()
                    .id("loading")
            }

            else -> {
                PostListEmptyRowEpoxyModel_()
                    .id("titleRow")
            }
        }
    }
}