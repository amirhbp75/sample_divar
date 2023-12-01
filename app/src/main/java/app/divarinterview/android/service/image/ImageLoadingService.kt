package app.divarinterview.android.service.image

import app.divarinterview.android.R
import app.divarinterview.android.view.DivarCustomImageView

interface ImageLoadingService {
    fun load(
        imageView: DivarCustomImageView,
        imageUrl: String,
        placeHolder: Int = R.drawable.bg_empty_state_thumbnail
    )
}