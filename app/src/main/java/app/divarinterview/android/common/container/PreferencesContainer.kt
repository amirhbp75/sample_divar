package app.divarinterview.android.common.container

import android.content.Context
import app.divarinterview.android.utils.sharedPreferences
import javax.inject.Inject

class PreferencesContainer @Inject constructor(context: Context) {

    // string data
    var token: String? by context.sharedPreferences("user_token", null)


}

