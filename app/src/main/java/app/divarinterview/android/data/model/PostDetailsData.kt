package app.divarinterview.android.data.model

import com.google.gson.annotations.SerializedName

data class PostDetailsData(
    @SerializedName("items")
    val sliderItem: List<PostDetailsSliderItem>? = null,
    @SerializedName("title")
    val headerTitle: String? = null,
    val subtitle: String? = null,
    @SerializedName("image_url")
    val headerImage: String? = null,
    @SerializedName("show_thumbnail")
    val showHeaderImage: Boolean? = null,
    @SerializedName("value")
    val rowTitle: String? = null,
    val text: String? = null,

)