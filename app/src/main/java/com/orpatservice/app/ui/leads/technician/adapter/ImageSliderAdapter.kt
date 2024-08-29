package com.orpatservice.app.ui.leads.technician.adapter

import android.R.attr.mode
import android.content.Context
import android.graphics.PointF
import android.view.*
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.orpatservice.app.R


class ImageSliderAdapter (private val context:Context,private val imageDataModelList: ArrayList<String>) : RecyclerView.Adapter<ImageSliderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_sliderscreen_image, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(imageDataModelList, context)
    }

    override fun getItemCount(): Int {
        return imageDataModelList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(imageDataModel: ArrayList<String>, context: Context) {
            val imageView = itemView.findViewById<ImageView>(R.id.img_slider)
            Glide.with(itemView.context).load(imageDataModel[position]).into(imageView)

        }


    }
}




