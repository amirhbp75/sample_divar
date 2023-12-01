package app.divarinterview.android.data.model

import androidx.annotation.StringRes

data class EmptyState(
    val mustShow: Boolean,
    val actionType: ActionType,
    @StringRes val title: Int = 0,
    @StringRes val subtitleRes: Int = 0,
    val subtitleText: String? = null,
    val actionButton: Boolean = false
) {
    enum class ActionType {
        RETURN, TRY_AGAIN
    }
}