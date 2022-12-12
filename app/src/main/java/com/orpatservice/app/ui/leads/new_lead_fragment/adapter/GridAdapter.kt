package com.orpatservice.app.ui.leads.new_lead_fragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.orpatservice.app.R
import com.orpatservice.app.data.model.requests_leads.ImageData
import com.orpatservice.app.data.model.requests_leads.LeadEnquiryImage
import com.orpatservice.app.databinding.ItemTechnicianComplaintBinding
import com.orpatservice.app.ui.leads.technician.response.TechnicianEnquiryImage
import com.orpatservice.app.utils.CommonUtils

internal class GridAdapter(
    private val context: Context,
    private val adapterPosition: Int,
    private val partsPosition: Int,
    private val imgList: ArrayList<LeadEnquiryImage>,
    private val itemClickListener: (Int, View,Int) -> Unit,
) :
    BaseAdapter(), ListAdapter {
    private var layoutInflater: LayoutInflater? = null
    private lateinit var imageView: ImageView
    private lateinit var imageView_hide: ImageView

    override fun getCount(): Int {
        return imgList.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        if (layoutInflater == null) {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if (convertView == null) {
            convertView = layoutInflater!!.inflate(R.layout.rowitem, null)
        }
        imageView = convertView!!.findViewById(R.id.imageView)
        imageView_hide = convertView.findViewById(R.id.imageView_hide)

        val selectedImageList: ArrayList<String> = ArrayList()
        val selectedImageList1: ArrayList<String> = ArrayList()
        for(i in CommonUtils.imageData) {
            if (i.adapter_Position == adapterPosition) {
              //  println("count" + i)
                selectedImageList.add(i.image_position.toString())
                //println("image_pos" + i.image_pos)
            }else{
               // println("count111" + i)
                selectedImageList1.add(i.image_position.toString())

            }
        }


        if(!selectedImageList.isEmpty() ){
            val selectImage = selectedImageList.distinct()
            val selectImage1 = selectedImageList1.distinct()
            if(selectImage.count() == 1){

                if (selectImage[0] == position.toString()) {
                    imageView_hide.visibility = VISIBLE
                    imageView.visibility = GONE
                    Glide.with(context)
                        .load(imgList[position].image)
                        .placeholder(R.color.gray)
                        .into(imageView_hide)

                } else {
                    imageView_hide.visibility = GONE
                    imageView.visibility = VISIBLE
                    Glide.with(context)
                        .load(imgList[position].image)
                        .placeholder(R.color.gray)
                        .into(imageView)
                }
            }else if(selectImage.count() == 2){
                if (selectImage[0] == position.toString() || selectImage[1] == position.toString()) {
                    imageView_hide.visibility = VISIBLE
                    imageView.visibility = GONE
                    Glide.with(context)
                        .load(imgList[position].image)
                        .placeholder(R.color.gray)
                        .into(imageView_hide)
                } else {
                    imageView_hide.visibility = GONE
                    imageView.visibility = VISIBLE
                    Glide.with(context)
                        .load(imgList[position].image)
                        .placeholder(R.color.gray)
                        .into(imageView)
                }
            }else if(selectImage.count() == 3){
                if (selectImage[0] == position.toString() || selectImage[1] == position.toString() || selectImage[2] == position.toString()) {

                    imageView_hide.visibility = VISIBLE
                    imageView.visibility = GONE
                    Glide.with(context)
                        .load(imgList[position].image)
                        .placeholder(R.color.gray)
                        .into(imageView_hide)
                } else {

                    imageView_hide.visibility = GONE
                    imageView.visibility = VISIBLE
                    Glide.with(context)
                        .load(imgList[position].image)
                        .placeholder(R.color.gray)
                        .into(imageView)
                }
            }else if(selectedImageList.count() == 4){
                if (selectImage[0] == position.toString() || selectImage[1] == position.toString() || selectImage[2] == position.toString() || selectImage[3] == position.toString()) {

                    imageView_hide.visibility = VISIBLE
                    imageView.visibility = GONE
                    Glide.with(context)
                        .load(imgList[position].image)
                        .placeholder(R.color.gray)
                        .into(imageView_hide)
                } else {

                    imageView_hide.visibility = GONE
                    imageView.visibility = VISIBLE
                    Glide.with(context)
                        .load(imgList[position].image)
                        .placeholder(R.color.gray)
                        .into(imageView)
                }
            }else if(selectedImageList.count() == 5){
                if (selectImage[0] == position.toString() || selectImage[1] == position.toString() || selectImage[2] == position.toString() || selectImage[3] == position.toString() || selectImage[4] == position.toString()) {

                    imageView_hide.visibility = VISIBLE
                    imageView.visibility = GONE
                    Glide.with(context)
                        .load(imgList[position].image)
                        .placeholder(R.color.gray)
                        .into(imageView_hide)
                } else {

                    imageView_hide.visibility = GONE
                    imageView.visibility = VISIBLE
                    Glide.with(context)
                        .load(imgList[position].image)
                        .placeholder(R.color.gray)
                        .into(imageView)
                }
            }
        }else{
            imageView_hide.visibility = GONE
            imageView.visibility = VISIBLE
            Glide.with(context)
                .load(imgList[position].image)
                .placeholder(R.color.gray)
                .into(imageView)
        }

        imageView.setOnClickListener {
            itemClickListener(
                position,
                imageView,
                adapterPosition
            )
        }

        return convertView
    }

}


