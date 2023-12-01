package app.divarinterview.android.ui.post.details.sdui

import app.divarinterview.android.data.model.PostDetailsSDUIWidget
import app.divarinterview.android.data.model.PostDetailsWidgetType
import com.airbnb.epoxy.TypedEpoxyController


class PostDetailsEpoxyController : TypedEpoxyController<List<PostDetailsSDUIWidget>>() {

    override fun buildModels(data: List<PostDetailsSDUIWidget>) {
        if (data.isEmpty()) {
            return
        }

        data.forEach { widgetData ->
            when (widgetData.widgetType) {
                PostDetailsWidgetType.IMAGE_SLIDER_ROW -> {
                    widgetData.data.let {
                        if (it.sliderItem != null)
                            postDetailsSlider {
                                id("slider")
                                slides(it.sliderItem)
                            }
                    }
                }

                PostDetailsWidgetType.HEADER_ROW -> {
                    widgetData.data.let {
                        if (it.title != null)
                            postDetailsHeaderRow {
                                id(it.title)
                                title(it.title)
                                subtitle(it.subtitle)
                                image(it.headerImage)
                            }
                    }
                }

                PostDetailsWidgetType.INFO_ROW -> {
                    widgetData.data.let {
                        if (it.title != null && it.value != null)
                            postDetailsInfoRow {
                                id(it.title)
                                title(it.title)
                                subtitle(it.value)
                            }
                    }
                }

                PostDetailsWidgetType.DESCRIPTION_ROW -> {
                    widgetData.data.let {
                        if (it.text != null)
                            postDetailsTextRow {
                                id(it.text)
                                text(it.text)
                            }
                    }
                }

                PostDetailsWidgetType.TITLE_ROW -> {
                    widgetData.data.let {
                        if (it.text != null)
                            postDetailsHeaderRow {
                                id(it.text)
                                title(it.text)
                            }
                    }
                }
            }
        }
    }
}