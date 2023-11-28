package app.divarinterview.android.service.image

import app.divarinterview.android.view.DivarCustomImageView
import com.facebook.drawee.view.SimpleDraweeView

class FrescoImageLoadingService : ImageLoadingService {
    override fun load(
        imageView: DivarCustomImageView,
        imageUrl: String,
        placeHolder: Int?
    ) {
        if (imageView is SimpleDraweeView) {
            imageView.setImageURI(imageUrl)
            placeHolder?.let { imageView.hierarchy.setPlaceholderImage(it) }
        } else
            throw IllegalStateException("Imageview must be instance of SimpleDraweeView")
    }
}