package com.orpatservice.app.ui.leads.customer_detail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.orpatservice.app.R
import com.orpatservice.app.data.model.requests_leads.LeadEnquiryImage

class SlideVerificationPartsAdater(private val context: Context, private val imageDataModelList: ArrayList<LeadEnquiryImage>) : RecyclerView.Adapter<SlideVerificationPartsAdater.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_slide_verification_parts, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(imageDataModelList, context)
    }

    override fun getItemCount(): Int {
        return imageDataModelList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(imageDataModel: ArrayList<LeadEnquiryImage>, context: Context) {
            val imageView = itemView.findViewById<ImageView>(R.id.img_slider)
            val tv_title = itemView.findViewById<TextView>(R.id.tv_title)
            val tv_status = itemView.findViewById<TextView>(R.id.tv_status)

            tv_title.setText(imageDataModel[position].part)
            tv_status.setText(imageDataModel[position].status)

            Glide.with(context).load(imageDataModel[position].image).into(imageView)

        }
    }
}




