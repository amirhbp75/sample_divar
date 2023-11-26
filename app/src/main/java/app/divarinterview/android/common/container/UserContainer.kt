package app.divarinterview.android.common.container


object UserContainer {
    var token: String? = null
        private set

    fun updateToken(token: String?) {
        UserContainer.token = token
    }
}