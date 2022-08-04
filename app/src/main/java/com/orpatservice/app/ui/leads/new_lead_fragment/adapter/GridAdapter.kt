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
    private val imgList: ArrayList<LeadEnquiryImage>,
    private val itemClickListener: (Int, View) -> Unit,
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
        ): View? {
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

            if(!CommonUtils.imageData.isEmpty()) {
                if(CommonUtils.imageData.count() == 1) {
                    //println("CommonUtils.imageData" + CommonUtils.imageData[0].image_data)
                    if (CommonUtils.imageData[0].image_data == position) {
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
                }else if(CommonUtils.imageData.count() == 2) {
                    if (CommonUtils.imageData[0].image_data == position ||  CommonUtils.imageData[1].image_data == position) {
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
                }else if(CommonUtils.imageData.count() == 3) {
                    if (CommonUtils.imageData[0].image_data == position ||  CommonUtils.imageData[1].image_data == position ||  CommonUtils.imageData[2].image_data == position) {
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
                }else if(CommonUtils.imageData.count() == 4) {
                    if (CommonUtils.imageData[0].image_data == position ||  CommonUtils.imageData[1].image_data == position ||  CommonUtils.imageData[2].image_data == position ||  CommonUtils.imageData[3].image_data == position) {
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
                }else if(CommonUtils.imageData.count() == 5) {
                   // println("CommonUtils.imageData" + CommonUtils.imageData[0].image_data)
                  //  println("CommonUtils.imageData1" + CommonUtils.imageData[1].image_data)
                    if (CommonUtils.imageData[0].image_data == position ||  CommonUtils.imageData[1].image_data == position ||  CommonUtils.imageData[2].image_data == position ||  CommonUtils.imageData[3].image_data == position ||  CommonUtils.imageData[4].image_data == position) {
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
                Glide.with(context)
                    .load(imgList[position].image)
                    .placeholder(R.color.gray)
                    .into(imageView)
            }

            imageView.setOnClickListener {
                itemClickListener(
                    position,
                    imageView,
                )
            }

            return convertView
        }
    }
