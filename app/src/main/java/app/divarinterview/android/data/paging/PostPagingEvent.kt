package app.divarinterview.android.data.paging

import androidx.annotation.StringRes

sealed class PostPagingEvent(
    val page: Int? = null,
    val message: Int = 0,
    val throwable: Throwable? = null
) {
    object Success : PostPagingEvent()
    class Loading(page: Int) : PostPagingEvent(page)
    class Error(
        @StringRes message: Int,
        throwable: Throwable? = null
    ) : PostPagingEvent(
        message = message,
        throwable = throwable
    )
}