package app.divarinterview.android.common.container


object UserContainer {
    var token: String? = null
        private set
    var cityId: Int = -1
        private set
    var cityName: String? = null
        private set

    fun updateToken(token: String?) {
        UserContainer.token = token
    }

    fun updateCity(cityId: Int, cityName: String?) {
        UserContainer.cityId = cityId
        UserContainer.cityName = cityName
    }
}