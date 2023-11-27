package app.divarinterview.android.common.container

import android.content.Context
import app.divarinterview.android.utils.sharedPreferences
import javax.inject.Inject

class PreferencesContainer @Inject constructor(context: Context) {

    // string data
    var token: String? by context.sharedPreferences("user_token", null)

    var cityName: String? by context.sharedPreferences("selected_city_name", null)


    // int data
    var cityId: Int by context.sharedPreferences("selected_city_id", -1)


}

