package app.divarinterview.android.ui.post.list.sdui

import app.divarinterview.android.data.model.PostItemSDUIWidget
import app.divarinterview.android.data.model.PostItemWidgetType
import com.airbnb.epoxy.TypedEpoxyController


class PostListEpoxyController : TypedEpoxyController<List<PostItemSDUIWidget>>() {

    override fun buildModels(data: List<PostItemSDUIWidget>) {
        if (data.isEmpty()) {
            return
        }

        data.forEach { widgetData ->
            when (widgetData.widgetType) {
                PostItemWidgetType.TITLE_ROW -> {
                    widgetData.data?.text?.let {
                        postListTitleRow {
                            id("titleRow")
                            text(it)
                        }
                    }
                }

                PostItemWidgetType.SUBTITLE_ROW -> {
                    widgetData.data?.text?.let {
                        postListSubtitleRow {
                            id("subtitleRow")
                            text(it)
                        }
                    }
                }

                PostItemWidgetType.POST_ROW -> {
                    widgetData.data?.let {
                        postListPostRow {
                            id(it.token)
                            data(it)
                        }
                    }
                }

                PostItemWidgetType.LOADING_ROW -> {
                    postListLoadingRow {
                        id("loading")
                    }
                }
            }
        }
    }
}