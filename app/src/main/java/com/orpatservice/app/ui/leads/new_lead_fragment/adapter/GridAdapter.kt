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
        /*for(i in CommonUtils.imageData) {
            if (i.partPosition == partPosition) {
           //     println("imgList.count()" + CommonUtils.imageData.count())
                println("image_pos" + i.image_pos)
            }else{
                println("elseimgList.count()" + i.image_pos)

            }
        }*/

        if (!CommonUtils.imageData.isEmpty()) {
            println("partsPosition" + partsPosition)
            println("adapterPosition" + adapterPosition)
            println("CommonUtils.imageData" + CommonUtils.imageData)

            // if(adapterPosition == CommonUtils.imageData[0].adapter_Position && partsPosition == CommonUtils.imageData[0].part_position) {

            if (CommonUtils.imageData.count() == 1) {
                //if(adapterPosition == CommonUtils.imageData[0].adapter_Position) {    //if(adapterPosition == CommonUtils.imageData[0].adapter_Position/* && partsPosition == CommonUtils.imageData[0].part_position*/) {
                println("1111111111111111 " + "111111111111  ")
                // if(partsPosition == CommonUtils.imageData[0].part_position) {
                println("222222222222222 " + "22222222222222222222222222  ")
                if (CommonUtils.imageData[0].image_position == position) {
                    println("333333333333333 " + "3333333333333333  ")
                    imageView_hide.visibility = VISIBLE
                    imageView.visibility = GONE
                    Glide.with(context)
                        .load(imgList[position].image)
                        .placeholder(R.color.gray)
                        .into(imageView_hide)

                } else {
                    println("44444444444444 " + "444444444444444  ")
                    imageView_hide.visibility = GONE
                    imageView.visibility = VISIBLE
                    Glide.with(context)
                        .load(imgList[position].image)
                        .placeholder(R.color.gray)
                        .into(imageView)
                }
                /*  }else{
                        //  println("partPosition nhi h" + "partPosition  nhi h ")
                        if (CommonUtils.imageData[0].image_position == position) {
                            println("5555555555555 " + "55555555555  ")
                            imageView_hide.visibility = VISIBLE
                            imageView.visibility = GONE
                            Glide.with(context)
                                .load(imgList[position].image)
                                .placeholder(R.color.gray)
                                .into(imageView_hide)

                        } else {
                            println("6666666666666666 " + "6666666666666666  ")
                            imageView_hide.visibility = GONE
                            imageView.visibility = VISIBLE
                            Glide.with(context)
                                .load(imgList[position].image)
                                .placeholder(R.color.gray)
                                .into(imageView)
                        }
                    }
                }else{
                    println("77777777777 " + "77777777777777777777777  ")
                }*/
                /* }else{
                     println("pta nhi"+"pta nhi ")
                     imageView_hide.visibility = GONE
                     imageView.visibility = VISIBLE
                     Glide.with(context)
                         .load(imgList[position].image)
                         .placeholder(R.color.gray)
                         .into(imageView)
                 }*/
            } else if (CommonUtils.imageData.count() == 2) {
                // if(adapterPosition == CommonUtils.imageData[0].adapter_Position || adapterPosition == CommonUtils.imageData[1].adapter_Position) {
                println("88888888888888 " + "888888888888888  ")
                //if(partsPosition == CommonUtils.imageData[0].part_position || partsPosition == CommonUtils.imageData[1].part_position) {
                println("99999999999 " + "999999999999999  ")
                if (CommonUtils.imageData[0].image_position == position || CommonUtils.imageData[1].image_position == position) {
                    println("10000000000000000 " + "10000000000000000  ")
                    imageView_hide.visibility = VISIBLE
                    imageView.visibility = GONE
                    Glide.with(context)
                        .load(imgList[position].image)
                        .placeholder(R.color.gray)
                        .into(imageView_hide)
                } else {
                    println("122222222222222 " + "1222222222222222222  ")
                    imageView_hide.visibility = GONE
                    imageView.visibility = VISIBLE
                    Glide.with(context)
                        .load(imgList[position].image)
                        .placeholder(R.color.gray)
                        .into(imageView)
                }
                /*}else{
                        if (CommonUtils.imageData[0].image_position == position || CommonUtils.imageData[1].image_position == position) {
                            println("13333333333333333 " + "1333333333333333  ")
                            imageView_hide.visibility = VISIBLE
                            imageView.visibility = GONE
                            Glide.with(context)
                                .load(imgList[position].image)
                                .placeholder(R.color.gray)
                                .into(imageView_hide)
                        } else {
                            println("144444444444444 " + "144444444444444  ")
                            imageView_hide.visibility = GONE
                            imageView.visibility = VISIBLE
                            Glide.with(context)
                                .load(imgList[position].image)
                                .placeholder(R.color.gray)
                                .into(imageView)
                        }
                    }
                }else {
                    println("15555555555 " + "15555555555  ")

                    imageView_hide.visibility = GONE
                    imageView.visibility = VISIBLE
                    Glide.with(context)
                        .load(imgList[position].image)
                        .placeholder(R.color.gray)
                        .into(imageView)
                }*/
            } else if (CommonUtils.imageData.count() == 3) {
                println("16666666666 " + "1666666666666  ")
                //if(adapterPosition == CommonUtils.imageData[0].adapter_Position || adapterPosition == CommonUtils.imageData[1].adapter_Position  || adapterPosition == CommonUtils.imageData[2].adapter_Position) {
                println("1777777777777 " + "17777777777  ")
                // if(partsPosition == CommonUtils.imageData[0].part_position || partsPosition == CommonUtils.imageData[1].part_position || partsPosition == CommonUtils.imageData[2].part_position) {
                println("1888888 " + "188888888888  ")
                if (CommonUtils.imageData[0].image_position == position || CommonUtils.imageData[1].image_position == position || CommonUtils.imageData[2].image_position == position) {
                    println("19999999999 " + "199999999  ")
                    imageView_hide.visibility = VISIBLE
                    imageView.visibility = GONE
                    Glide.with(context)
                        .load(imgList[position].image)
                        .placeholder(R.color.gray)
                        .into(imageView_hide)
                } else {
                    println("200000000000000 " + "20000000000  ")
                    imageView_hide.visibility = GONE
                    imageView.visibility = VISIBLE
                    Glide.with(context)
                        .load(imgList[position].image)
                        .placeholder(R.color.gray)
                        .into(imageView)
                }
                /* }else{

                        if (CommonUtils.imageData[0].image_position == position || CommonUtils.imageData[1].image_position == position || CommonUtils.imageData[2].image_position == position) {
                            println("21111111111111111 " + "21111111111111111  ")
                            imageView_hide.visibility = VISIBLE
                            imageView.visibility = GONE
                            Glide.with(context)
                                .load(imgList[position].image)
                                .placeholder(R.color.gray)
                                .into(imageView_hide)
                        } else {
                            println("222222222222222 " + "222222222222  ")
                            imageView_hide.visibility = GONE
                            imageView.visibility = VISIBLE
                            Glide.with(context)
                                .load(imgList[position].image)
                                .placeholder(R.color.gray)
                                .into(imageView)
                        }
                    }
                }else{
                    println("2333333333333333333 " + "23333333333333  ")
                    imageView_hide.visibility = GONE
                    imageView.visibility = VISIBLE
                    Glide.with(context)
                        .load(imgList[position].image)
                        .placeholder(R.color.gray)
                        .into(imageView)
                }*/
            } else if (CommonUtils.imageData.count() == 4) {
                println("24444444444 " + "24444444444  ")
                //if(adapterPosition == CommonUtils.imageData[0].adapter_Position || adapterPosition == CommonUtils.imageData[1].adapter_Position  || adapterPosition == CommonUtils.imageData[2].adapter_Position || adapterPosition == CommonUtils.imageData[3].adapter_Position) {
                println("255555555555555 " + "2555555555555  ")
                // if(partsPosition == CommonUtils.imageData[0].part_position || partsPosition == CommonUtils.imageData[1].part_position || partsPosition == CommonUtils.imageData[2].part_position || partsPosition == CommonUtils.imageData[3].part_position) {
                println("26666666666 " + "26666666666  ")
                if (CommonUtils.imageData[0].image_position == position || CommonUtils.imageData[1].image_position == position || CommonUtils.imageData[2].image_position == position || CommonUtils.imageData[3].image_position == position) {
                    println("2777777777777 " + "27777777  ")
                    imageView_hide.visibility = VISIBLE
                    imageView.visibility = GONE
                    Glide.with(context)
                        .load(imgList[position].image)
                        .placeholder(R.color.gray)
                        .into(imageView_hide)
                } else {
                    println("2777777777777 " + "27777777  ")
                    imageView_hide.visibility = GONE
                    imageView.visibility = VISIBLE
                    Glide.with(context)
                        .load(imgList[position].image)
                        .placeholder(R.color.gray)
                        .into(imageView)
                }
                /* }else{
                        if (CommonUtils.imageData[0].image_position == position || CommonUtils.imageData[1].image_position == position || CommonUtils.imageData[2].image_position == position || CommonUtils.imageData[3].image_position == position) {
                            println("28888888 " + "28888888888  ")
                            imageView_hide.visibility = VISIBLE
                            imageView.visibility = GONE
                            Glide.with(context)
                                .load(imgList[position].image)
                                .placeholder(R.color.gray)
                                .into(imageView_hide)
                        } else {
                            println("29999999 " + "299999999  ")
                            imageView_hide.visibility = GONE
                            imageView.visibility = VISIBLE
                            Glide.with(context)
                                .load(imgList[position].image)
                                .placeholder(R.color.gray)
                                .into(imageView)
                        }
                    }
                }else{
                    println("30000000 " + "30000000000  ")
                    imageView_hide.visibility = GONE
                    imageView.visibility = VISIBLE
                    Glide.with(context)
                        .load(imgList[position].image)
                        .placeholder(R.color.gray)
                        .into(imageView)
                }*/

            } else if (CommonUtils.imageData.count() == 5) {
                // if (adapterPosition == CommonUtils.imageData[0].adapter_Position || adapterPosition == CommonUtils.imageData[1].adapter_Position || adapterPosition == CommonUtils.imageData[2].adapter_Position || adapterPosition == CommonUtils.imageData[3].adapter_Position || adapterPosition == CommonUtils.imageData[4].adapter_Position) {
                // if(partsPosition == CommonUtils.imageData[0].part_position || partsPosition == CommonUtils.imageData[1].part_position || partsPosition == CommonUtils.imageData[2].part_position || partsPosition == CommonUtils.imageData[3].part_position || partsPosition == CommonUtils.imageData[4].part_position) {

                if (CommonUtils.imageData[0].image_position == position || CommonUtils.imageData[1].image_position == position || CommonUtils.imageData[2].image_position == position || CommonUtils.imageData[3].image_position == position || CommonUtils.imageData[4].image_position == position) {
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
                /*}else{
                        if (CommonUtils.imageData[0].image_position == position || CommonUtils.imageData[1].image_position == position || CommonUtils.imageData[2].image_position == position || CommonUtils.imageData[3].image_position == position  || CommonUtils.imageData[4].image_position == position) {
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
            }*/
                /*   }else{
                   Glide.with(context)
                       .load(imgList[position].image)
                       .placeholder(R.color.gray)
                       .into(imageView)
                   println("image_adapter_pos"+i.partPosition)
               }*/
                //}

                /* } else {
                Glide.with(context)
                    .load(imgList[position].image)
                    .placeholder(R.color.gray)
                    .into(imageView)*/
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
                    adapterPosition
                )
            }

            return convertView
        }

}


