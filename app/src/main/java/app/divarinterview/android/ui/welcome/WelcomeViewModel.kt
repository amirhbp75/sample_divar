package app.divarinterview.android.ui.welcome

import app.divarinterview.android.common.BaseViewModel
import app.divarinterview.android.common.USER_TOKEN
import app.divarinterview.android.common.container.PreferencesContainer
import app.divarinterview.android.common.container.UserContainer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val preferences: PreferencesContainer
) : BaseViewModel() {

    fun updateUserToken() {
        if (preferences.token != USER_TOKEN) {
            preferences.token = USER_TOKEN
            UserContainer.updateToken(USER_TOKEN)
        }
    }
}