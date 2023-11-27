package app.divarinterview.android.ui.city

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.divarinterview.android.R
import app.divarinterview.android.data.model.City
import javax.inject.Inject

class CityListAdapter @Inject constructor() :
    ListAdapter<City, CityListAdapter.ViewHolder>(MyDiffUtilCallback()) {

    private var onItemClickListener: ((City) -> Unit)? = null

    fun setOnItemClickListener(listener: (City) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_city, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
        holder.itemView.apply {
            setOnClickListener {
                onItemClickListener?.let { click ->
                    click(currentItem)
                }
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cityNameTv: TextView = itemView.findViewById(R.id.cityNameTv)

        fun bind(city: City) {
            cityNameTv.text = city.name
        }
    }

    private class MyDiffUtilCallback : DiffUtil.ItemCallback<City>() {
        override fun areItemsTheSame(oldItem: City, newItem: City): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: City, newItem: City): Boolean {
            return areItemsTheSame(oldItem, newItem)
        }
    }
}