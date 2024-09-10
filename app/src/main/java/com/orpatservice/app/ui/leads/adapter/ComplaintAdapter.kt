package com.orpatservice.app.ui.leads.adapter

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.orpatservice.app.R
import com.orpatservice.app.data.model.requests_leads.Enquiry
import com.orpatservice.app.data.model.requests_leads.LeadData
import com.orpatservice.app.databinding.ItemComplaintBinding
import com.orpatservice.app.utils.CommonUtils
import java.util.*

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
                holder.onBind(enquiryArrayList[position],leadDataArrayList,enquiryArrayList.count(), itemClickListener)
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
            tatalCount: Int,
            itemClickListener: (Int, View,ItemComplaintBinding) -> Unit
        ) {
            if(!enquiry.installation_link.isNullOrEmpty()||!enquiry.service_link.isNullOrEmpty()){
                binding.ivLink.visibility =  VISIBLE
                binding.tvInstallation.visibility =  VISIBLE

            }else{
                binding.ivLink.visibility =  GONE
                binding.tvInstallation.visibility =  GONE
            }

            if(leadData.service_request_type.equals("installation",true)){
                binding.tvInstallation.setText("Watch Installation")

            }else{
                binding.tvInstallation.setText("Watch Service")

            }

            binding.tvServiceCenterDescriptionValue.setText(enquiry.service_center_discription ?: "")
            binding.tvCallCenterNoteValue.setText(enquiry.orpat_description)
            binding.edtInvoiceNumberValue.setText(enquiry.invoice_no ?: "")
            binding.edtSelectInvoiceDate.setText(enquiry.purchase_at ?: "")
            binding.edtBuyerName.setText(enquiry.buyer_name ?: "")
            binding.edtGstNumber.setText(enquiry.seller_gst_no ?: "")
            binding.imgInvoiceImage.setOnClickListener {
                itemClickListener(
                    adapterPosition,
                    binding.imgInvoiceImage,binding
                )
            }
            //  binding.tvModelNameValue.text = enquiry.id
            //binding.tvWarrantyStatusValue.text = enquiry.in_warranty
            if(enquiry.detail_status == 1) {
                binding.imgUpdatedTask.visibility = View.VISIBLE
            }else {
                binding.imgUpdatedTask.visibility = View.GONE
            }
                /*if(enquiry.purchase_at != null) {
                    if (!enquiry.warranty_parts.isEmpty()) {
                        binding.tvServiceableWarrantyParts.visibility = VISIBLE
                    } else {
                        binding.tvServiceableWarrantyParts.visibility = GONE
                    }
                }else{
                    binding.tvServiceableWarrantyParts.visibility = GONE
                }*/



                if(enquiry.seller_gst_no != null){
                    binding.tvGstFirstName.visibility = VISIBLE
                    binding.tvGstTradeName.visibility = VISIBLE
                    binding.tvGstName.visibility = VISIBLE
                    binding.tvGstTrade.visibility = VISIBLE

                    binding.tvGstFirstName.setText(enquiry.seller_name)
                    binding.tvGstTradeName.setText(enquiry.seller_trade_name)
                }else{
                    binding.tvGstFirstName.visibility = GONE
                    binding.tvGstTradeName.visibility = GONE
                    binding.tvGstName.visibility = GONE
                    binding.tvGstTrade.visibility = GONE
                }

                if(enquiry.in_warranty.equals("Yes")){
                    binding.radiobtnYes.isChecked = true

                    binding.radiobtnNo.isChecked = false
                    binding.radiobtnNotSure.isChecked = false


                }else if(enquiry.in_warranty.equals("No")){
                    binding.radiobtnNo.isChecked = true

                    binding.radiobtnYes.isChecked = false
                    binding.radiobtnNotSure.isChecked = false

                }else if(enquiry.in_warranty.equals("Not Sure")){
                    binding.radiobtnNotSure.isChecked = true

                    binding.radiobtnYes.isChecked = false
                    binding.radiobtnNo.isChecked = false
                }

         //   }else{
                if(enquiry.in_warranty.equals("No")){
                    binding.radiobtnYes.isEnabled = false
                    binding.radiobtnYes.isChecked = false
                  //  binding.radiobtnNo.isEnabled = false
                    binding.radiobtnNotSure.isEnabled = false
                    binding.radiobtnNotSure.isChecked = false

                    binding.radiobtnNo.isChecked = true

                    binding.liUpdate.visibility = GONE
                    binding.liGenerateCancel.visibility = VISIBLE

                }

                binding.imgUpdatedTask.visibility = View.GONE
                binding.btnUpdate.visibility = View.VISIBLE
                binding.btnHideUpdate.visibility = View.GONE
                // binding.tvServiceableWarrantyParts.visibility = GONE

                binding.imgInvoiceImage.setOnClickListener {
                    itemClickListener(
                        adapterPosition,
                        binding.imgInvoiceImage,binding
                    )
                }
            //}

            binding.tvDescriptionValue.text = enquiry.customer_discription
            binding.tvModelNameValue.text = enquiry.model_no
            binding.tvComplaintPresetValue.text = enquiry.complaint_preset

            if(!enquiry.warranty_parts.isEmpty()){
                binding.tvServiceableWarrantyParts.visibility = VISIBLE
            }else {
                binding.tvServiceableWarrantyParts.visibility = GONE
            }

            if(!binding.tvServiceCenterDescriptionValue.text.isNullOrEmpty()){
                binding.tvErrorDes.visibility = GONE
            }else{
                binding.tvErrorDes.visibility = VISIBLE
            }
            /* if(!binding.edtInvoiceNumberValue.text.isNullOrEmpty()){
                 binding.tvErrorInvoiceNumber.visibility = GONE
             }else{
                 binding.tvErrorInvoiceNumber.visibility = VISIBLE
             }

             if(!binding.edtSelectInvoiceDate.text.isNullOrEmpty()){
                 binding.tvErrorInvoiceDate.visibility = GONE
             }else{
                 binding.tvErrorInvoiceDate.visibility = VISIBLE
             }*/

            // enquiry.purchase_at?.let { binding.tvPurchaseAt.text = CommonUtils.dateFormat(it) }
            binding.tvServiceCenterDescriptionValue.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if(s!!.isEmpty()){
                        //   binding.tvErrorDes.visibility = View.VISIBLE
                    }else{
                        binding.tvErrorDes.visibility = View.GONE
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    //  validationUtil()
                    if(s!!.isEmpty()){
                        //  binding.tvErrorDes.visibility = View.VISIBLE
                    }else{
                        binding.tvErrorDes.visibility = View.GONE
                    }

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    if(count == 0){
                        //  binding.tvErrorDes.visibility = View.VISIBLE
                    }else {
                        binding.tvErrorDes.visibility = View.GONE
                    }
                }
            })

            binding.edtInvoiceNumberValue.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if(s!!.isEmpty()){
                        // binding.tvErrorInvoiceNumber.visibility = View.VISIBLE
                    }else{
                        binding.tvErrorInvoiceNumber.visibility = View.GONE
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    if(s!!.isEmpty()){
                        // binding.tvErrorInvoiceNumber.visibility = View.VISIBLE
                    }else{
                        binding.tvErrorInvoiceNumber.visibility = View.GONE
                    }
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if(count == 0){
                        //binding.tvErrorInvoiceNumber.visibility = View.VISIBLE
                    }else {
                        binding.tvErrorInvoiceNumber.visibility = View.GONE
                    }
                }
            })

            if(enquiry.is_audit_verified == "0") {
                binding.tvGstNumber.visibility = VISIBLE
                binding.liGstNum.visibility = VISIBLE


                binding.edtGstNumber.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        if (s!!.isEmpty()) {
                            // binding.tvErrorInvoiceNumber.visibility = View.VISIBLE
                        } else {
                            binding.tvErrorGstNumber.visibility = View.GONE
                        }
                    }

                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                        if (s!!.isEmpty()) {
                            // binding.tvErrorInvoiceNumber.visibility = View.VISIBLE
                        } else {
                            binding.tvErrorGstNumber.visibility = View.GONE
                        }
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        if (count == 0) {
                            //binding.tvErrorInvoiceNumber.visibility = View.VISIBLE
                        } else {
                            binding.tvErrorGstNumber.visibility = View.GONE
                        }
                    }
                })
            }else{
                binding.tvGstNumber.visibility = GONE
                binding.liGstNum.visibility = GONE
                binding.tvErrorGstNumber.visibility = GONE
            }

            binding.tvSubComplaintPresetValue.setOnClickListener {
                /* val subComplaintPresetData = adapterPosition?.let { it1 ->
                     SubComplaintPresetData(
                         "",it1
                     )
                 }
                 if (subComplaintPresetData != null) {
                     CommonUtils.subComplaintPresetData.add(subComplaintPresetData)
                 }
 */

                itemClickListener(
                    adapterPosition,
                    binding.tvSubComplaintPresetValue,binding
                )
            }


            if(enquiry.is_audit_verified.toString() == "1"){

                binding.btnUploadInvoices.setFocusable(false);
                binding.btnUploadInvoices.setEnabled(false);
                binding.btnUploadInvoices.setCursorVisible(false);
                binding.btnUploadInvoices.setKeyListener(null);

                binding.imgInvoiceImage.setFocusable(false);
                binding.imgInvoiceImage.setEnabled(false);

                binding.edtInvoiceNumberValue.setFocusable(false);
                binding.edtInvoiceNumberValue.setEnabled(false);
                binding.edtInvoiceNumberValue.setCursorVisible(false);
                binding.edtInvoiceNumberValue.setKeyListener(null);

                binding.edtSelectInvoiceDate.setFocusable(false);
                binding.edtSelectInvoiceDate.setEnabled(false);
                binding.edtSelectInvoiceDate.setCursorVisible(false);
                binding.edtSelectInvoiceDate.setKeyListener(null);

                binding.edtBuyerName.setFocusable(false);
                binding.edtBuyerName.setEnabled(false);
                binding.edtBuyerName.setCursorVisible(false);
                binding.edtBuyerName.setKeyListener(null);

                binding.edtGstNumber.setFocusable(false);
                binding.edtGstNumber.setEnabled(false);
                binding.edtGstNumber.setCursorVisible(false);
                binding.edtGstNumber.setKeyListener(null);

                binding.tvVerifyGst.setFocusable(false);
                binding.tvVerifyGst.setEnabled(false);
                binding.tvVerifyGst.setCursorVisible(false);
                binding.tvVerifyGst.setKeyListener(null);

            }


            binding.tvRemoveImage.setOnClickListener {
                itemClickListener(
                    adapterPosition,
                    binding.tvRemoveImage,binding
                )
            }

            binding.tvServiceableWarrantyParts.setOnClickListener {
                itemClickListener(
                    adapterPosition,
                    binding.tvServiceableWarrantyParts,binding
                )
            }
            binding.tvVerifyGst.setOnClickListener {
                itemClickListener(
                    adapterPosition,
                    binding.tvVerifyGst,binding
                )
            }

            binding.rbGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
                binding.tvErrorUnderWarranty.visibility = GONE
            })

            Glide.with(binding.imgInvoiceImage.context)
                .load(enquiry.invoice_url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                //.circleCrop() // .error(R.drawable.active_dot)
                .placeholder(R.drawable.ic_no_invoice)
                .into(binding.imgInvoiceImage)

            Glide.with(binding.ivQrCodeImage.context)
                .load(enquiry.dummy_barcode)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                //.circleCrop() // .error(R.drawable.active_dot)
                .placeholder(R.color.gray)
                .into(binding.ivQrCodeImage)
            binding.tvQrCodeNumber.text = enquiry.scanned_barcode

            /* binding.ivInvoiceImage.setOnClickListener {
                 itemClickListener(
                     adapterPosition,
                     binding.ivInvoiceImage,binding
                 )
             }*/
            binding.imgClose.setOnClickListener {
                itemClickListener(
                    adapterPosition,
                    binding.imgClose,binding
                )
            }
            binding.btnUploadInvoices.setOnClickListener{
                itemClickListener(
                    adapterPosition,
                    binding.btnUploadInvoices,binding
                )
            }
            binding.edtSelectInvoiceDate.setOnClickListener{
                itemClickListener(
                    adapterPosition,
                    binding.edtSelectInvoiceDate,binding
                )
            }
            binding.imgInvoiceImage.setOnClickListener {
                itemClickListener(
                    adapterPosition,
                    binding.imgInvoiceImage,binding
                )
            }

            val posi = adapterPosition+1

            binding.tvTask.setText("Task"+" "+ posi+"/"+tatalCount)

            binding.radiobtnYes.setOnClickListener {
                itemClickListener(
                    adapterPosition,
                    binding.radiobtnYes,binding
                )
            }
            binding.radiobtnNo.setOnClickListener {
                itemClickListener(
                    adapterPosition,
                    binding.radiobtnNo,binding
                )
            }
            binding.radiobtnNotSure.setOnClickListener {
                itemClickListener(
                    adapterPosition,
                    binding.radiobtnNotSure,binding
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

            binding.btnGstCancel.setOnClickListener {
                itemClickListener(
                    adapterPosition,
                    binding.btnGstCancel,
                    binding)
            }
            binding.btnGstGenerateChargeable.setOnClickListener {
                itemClickListener(
                    adapterPosition,
                    binding.btnGstGenerateChargeable,
                    binding)
            }

            binding.ivLink.setOnClickListener {

                itemClickListener(
                    adapterPosition,
                    binding.ivLink,
                    binding)

            }

        }
    }
}