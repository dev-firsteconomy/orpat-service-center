package com.orpatservice.app.ui.leads.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.orpatservice.app.R
import com.orpatservice.app.data.model.requests_leads.Enquiry
import com.orpatservice.app.data.model.requests_leads.LeadData
import com.orpatservice.app.databinding.ItemComplaintBinding
import com.orpatservice.app.utils.CommonUtils

/**
 * Created by Ajay Yadav on 22/12/21.
 */
class ComplaintAdapter(
    private val enquiryArrayList: ArrayList<Enquiry>,
    private val leadDataArrayList: LeadData,
    private val itemClickListener: (Int, View,ItemComplaintBinding) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemComplaintBinding =
            ItemComplaintBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ComplaintViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ComplaintViewHolder -> {
                holder.onBind(enquiryArrayList[position],leadDataArrayList, itemClickListener)
            }
        }
    }

    override fun getItemCount(): Int {
        return enquiryArrayList.size
    }

    class ComplaintViewHolder(private val binding: ItemComplaintBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(
            enquiry: Enquiry,
            leadData: LeadData,
            itemClickListener: (Int, View,ItemComplaintBinding) -> Unit
        ) {

          //  binding.tvModelNameValue.text = enquiry.id
            //binding.tvWarrantyStatusValue.text = enquiry.in_warranty
            if(enquiry.detail_status == 1){
                binding.btnUpdate.visibility = View.GONE
                binding.btnHideUpdate.visibility = View.VISIBLE
            }else{
                binding.btnUpdate.visibility = View.VISIBLE
                binding.btnHideUpdate.visibility = View.GONE
            }
            binding.tvDescriptionValue.text = enquiry.customer_discription
            binding.tvModelNameValue.text = enquiry.model_no

           // enquiry.purchase_at?.let { binding.tvPurchaseAt.text = CommonUtils.dateFormat(it) }

            Glide.with(binding.ivInvoiceImage.context)
                .load(enquiry.invoice_url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                //.circleCrop() // .error(R.drawable.active_dot)
                .placeholder(R.color.gray)
                .into(binding.ivInvoiceImage)

            Glide.with(binding.ivQrCodeImage.context)
                .load(enquiry.dummy_barcode)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                //.circleCrop() // .error(R.drawable.active_dot)
                .placeholder(R.color.gray)
                .into(binding.ivQrCodeImage)
            binding.tvQrCodeNumber.text = enquiry.scanned_barcode
            binding.ivInvoiceImage.setOnClickListener {
                itemClickListener(
                    adapterPosition,
                    binding.ivInvoiceImage,binding
                )
            }
          /*  binding.btnUploadInvoice.setOnClickListener{
                itemClickListener(
                    adapterPosition,
                    binding.btnUploadInvoice,binding
                )
            }*/
            binding.edtSelectInvoiceDate.setOnClickListener{
                itemClickListener(
                    adapterPosition,
                    binding.edtSelectInvoiceDate,binding
                )
            }

            val posi = adapterPosition+1
            val enquaryData = leadData.enquiries
            val data = ArrayList<String>()
            data.add(enquaryData.toString())
            val enquaryDataCount = data.count()
            val totalCount = enquaryDataCount+1

            binding.tvTask.setText("Task"+" "+ posi+"/"+totalCount)
           /* if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "")
                    .equals(Constants.SERVICE_CENTER)
            ) {
               // binding.btnCloseComplaint.visibility = View.GONE

            }

            if (!enquiry.status) {
                //binding.btnCloseComplaint.backgroundTintList = null

            } else {
               // binding.btnCloseComplaint.backgroundTintList =
                   *//* ColorStateList.valueOf(
                        ContextCompat.getColor(
                            binding.btnCloseComplaint.context,
                            R.color.gray
                        )
                    )*//*

            }*/
            binding.radiobtnYes.setOnClickListener {
                itemClickListener(
                    adapterPosition,
                    binding.ivQrCodeImage,binding
                )
            }
            binding.radiobtnNo.setOnClickListener {
                itemClickListener(
                    adapterPosition,
                    binding.ivQrCodeImage,binding
                )
            }
            binding.ivQrCodeImage.setOnClickListener {
                itemClickListener(
                    adapterPosition,
                    binding.ivQrCodeImage,binding
                )
            }
            binding.btnUpdate.setOnClickListener {
                itemClickListener(
                    adapterPosition,
                    binding.btnUpdate,
                    binding)
            }
           /*binding.rbGroup.setOnClickListener {
               itemClickListener(
                   adapterPosition,
                   binding.btnUpdate)
           }*/
            /*binding.btnCloseComplaint.setOnClickListener {
                itemClickListener(
                    adapterPosition,
                    binding.btnCloseComplaint
                )
            }*/

        }
    }
}