package app.divarinterview.android

import android.app.Application
import app.divarinterview.android.common.USER_TOKEN
import app.divarinterview.android.common.container.PreferencesContainer
import app.divarinterview.android.common.container.UserContainer
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var preferences: PreferencesContainer

    override fun onCreate() {
        super.onCreate()

        if (preferences.token != null)
            UserContainer.updateToken(preferences.token)
        else {
            preferences.token = USER_TOKEN
            UserContainer.updateToken(USER_TOKEN)
        }

        UserContainer.updateCity(preferences.cityId, preferences.cityName)
    }
}