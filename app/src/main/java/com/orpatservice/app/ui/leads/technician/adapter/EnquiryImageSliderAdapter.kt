package com.orpatservice.app.ui.leads.technician.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.orpatservice.app.R
import com.orpatservice.app.data.model.requests_leads.ImageListData
import com.orpatservice.app.ui.leads.technician.response.TechnicianEnquiryImage

class EnquiryImageSliderAdapter(private val imageDataModelList: ArrayList<TechnicianEnquiryImage>) : RecyclerView.Adapter<EnquiryImageSliderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_sliderscreen_image, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(imageDataModelList[position])
    }

    override fun getItemCount(): Int {
        return imageDataModelList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(imageDataModel: TechnicianEnquiryImage) {
            val imageView = itemView.findViewById<ImageView>(R.id.img_slider)

            Glide.with(itemView.context).load(imageDataModel.image).into(imageView)
        }
    }
}