package app.divarinterview.android.data.model

import com.google.gson.annotations.SerializedName

data class PostDetailsSDUIResponse(
    @SerializedName("widgets")
    val widgetList: List<PostDetailsSDUIWidget>,
    @SerializedName("enable_contact")
    val enableContact: Boolean,
    @SerializedName("contact_button_text")
    val contactButtonText: String
)
