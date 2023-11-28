package app.divarinterview.android.service.image

import app.divarinterview.android.view.DivarCustomImageView

interface ImageLoadingService {
    fun load(imageView: DivarCustomImageView, imageUrl: String, placeHolder: Int? = null)
}