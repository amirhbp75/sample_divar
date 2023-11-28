package app.divarinterview.android.di

import app.divarinterview.android.service.image.FrescoImageLoadingService
import app.divarinterview.android.service.image.ImageLoadingService

object DependencyInjector {

    fun getImageLoadingService(): ImageLoadingService {
        return FrescoImageLoadingService()
    }
}