package app.divarinterview.android.service.location

import android.location.Location

interface LocationTracker {
    suspend fun getCurrentLocation(): Location?
}