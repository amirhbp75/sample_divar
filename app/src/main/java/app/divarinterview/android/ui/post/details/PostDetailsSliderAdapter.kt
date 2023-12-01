package app.divarinterview.android.ui.post.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.divarinterview.android.R
import app.divarinterview.android.data.model.PostDetailsSliderItem
import app.divarinterview.android.service.image.ImageLoadingService
import app.divarinterview.android.view.DivarCustomImageView

class PostDetailsSliderAdapter(
    private val slides: List<PostDetailsSliderItem>,
    private val imageLoadingService: ImageLoadingService
) : RecyclerView.Adapter<PostDetailsSliderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.sdui_post_details_slider_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageUrl = slides[position].slideImage
        imageUrl?.let {
            imageLoadingService.load(
                holder.imageView,
                it,
                R.drawable.bg_empty_state_thumbnail_corner_0dp
            )
        }
    }

    override fun getItemCount(): Int {
        return slides.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: DivarCustomImageView = itemView.findViewById(R.id.slideIv)
    }
}