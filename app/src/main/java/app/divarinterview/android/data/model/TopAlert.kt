package app.divarinterview.android.data.model

import androidx.annotation.StringRes

data class TopAlert(
    val mustShow: Boolean,
    val loading: Boolean = false,
    @StringRes val text: Int = 0
)