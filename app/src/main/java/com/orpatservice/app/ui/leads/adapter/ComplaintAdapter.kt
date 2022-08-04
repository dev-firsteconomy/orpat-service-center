package com.orpatservice.app.ui.leads.adapter

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.RadioGroup
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

          //  binding.tvModelNameValue.text = enquiry.id
            //binding.tvWarrantyStatusValue.text = enquiry.in_warranty
            if(enquiry.detail_status == 1){
                binding.btnUpdate.visibility = View.GONE
                binding.btnHideUpdate.visibility = View.VISIBLE

                binding.tvServiceCenterDescriptionValue.setText(enquiry.service_center_discription)
                binding.tvServiceCenterDescriptionValue.setFocusable(false);
                binding.tvServiceCenterDescriptionValue.setEnabled(false);
                binding.tvServiceCenterDescriptionValue.setCursorVisible(false);
                binding.tvServiceCenterDescriptionValue.setKeyListener(null);
               /// binding.tvServiceCenterDescriptionValue.setBackgroundColor(Color.TRANSPARENT);

                binding.edtInvoiceNumberValue.setText(enquiry.invoice_no)
                binding.edtInvoiceNumberValue.setFocusable(false);
                binding.edtInvoiceNumberValue.setEnabled(false);
                binding.edtInvoiceNumberValue.setCursorVisible(false);
                binding.edtInvoiceNumberValue.setKeyListener(null);
               // binding.edtInvoiceNumberValue.setBackgroundColor(Color.TRANSPARENT);

                binding.edtSelectInvoiceDate.setText(enquiry.purchase_at)
                binding.edtSelectInvoiceDate.setFocusable(false);
                binding.edtSelectInvoiceDate.setEnabled(false);
                binding.edtSelectInvoiceDate.setCursorVisible(false);
                binding.edtSelectInvoiceDate.setKeyListener(null);
               // binding.edtSelectInvoiceDate.setBackgroundColor(Color.TRANSPARENT);

                binding.edtSellerName.setText("")
                binding.edtSellerName.setFocusable(false);
                binding.edtSellerName.setEnabled(false);
                binding.edtSellerName.setCursorVisible(false);
                binding.edtSellerName.setKeyListener(null);
               // binding.edtSellerName.setBackgroundColor(Color.TRANSPARENT);

                binding.edtGstNumber.setText("")
                binding.edtGstNumber.setFocusable(false);
                binding.edtGstNumber.setEnabled(false);
                binding.edtGstNumber.setCursorVisible(false);
                binding.edtGstNumber.setKeyListener(null);
               // binding.edtGstNumber.setBackgroundColor(Color.TRANSPARENT);


                binding.tvVerifyGst.setFocusable(false);
                binding.tvVerifyGst.setEnabled(false);
                binding.tvVerifyGst.setCursorVisible(false);
                binding.tvVerifyGst.setKeyListener(null);
                // binding.edtGstNumber.setBackgroundColor(Color.TRANSPARENT);

                binding.btnUploadInvoice.isEnabled = false
                binding.btnUploadInvoice.isClickable = false

                if(enquiry.in_warranty.equals("Yes")){
                    binding.radiobtnYes.isChecked = true

                    binding.radiobtnNo.isChecked = false
                    binding.radiobtnNotSure.isChecked = false

                    binding.radiobtnNo.setFocusable(false);
                    binding.radiobtnNo.setEnabled(false);
                    binding.radiobtnNo.setCursorVisible(false);
                    binding.radiobtnNo.setKeyListener(null);


                    binding.radiobtnNotSure.setFocusable(false);
                    binding.radiobtnNotSure.setEnabled(false);
                    binding.radiobtnNotSure.setCursorVisible(false);
                    binding.radiobtnNotSure.setKeyListener(null);

                }else if(enquiry.in_warranty.equals("No")){
                    binding.radiobtnNo.isChecked = true

                    binding.radiobtnYes.isChecked = false
                    binding.radiobtnNotSure.isChecked = false

                    binding.radiobtnYes.setFocusable(false);
                    binding.radiobtnYes.setEnabled(false);
                    binding.radiobtnYes.setCursorVisible(false);
                    binding.radiobtnYes.setKeyListener(null);


                    binding.radiobtnNotSure.setFocusable(false);
                    binding.radiobtnNotSure.setEnabled(false);
                    binding.radiobtnNotSure.setCursorVisible(false);
                    binding.radiobtnNotSure.setKeyListener(null);

                }else if(enquiry.in_warranty.equals("Not Sure")){
                    binding.radiobtnNotSure.isChecked = true

                    binding.radiobtnYes.isChecked = false
                    binding.radiobtnNo.isChecked = false

                    binding.radiobtnYes.setFocusable(false);
                    binding.radiobtnYes.setEnabled(false);
                    binding.radiobtnYes.setCursorVisible(false);
                    binding.radiobtnYes.setKeyListener(null);


                    binding.radiobtnNo.setFocusable(false);
                    binding.radiobtnNo.setEnabled(false);
                    binding.radiobtnNo.setCursorVisible(false);
                    binding.radiobtnNo.setKeyListener(null);
                }


            }else{
                binding.btnUpdate.visibility = View.VISIBLE
                binding.btnHideUpdate.visibility = View.GONE
            }


            binding.tvDescriptionValue.text = enquiry.customer_discription
            binding.tvModelNameValue.text = enquiry.model_no
            binding.tvComplaintPresetValue.text = enquiry.complaint_preset

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

            binding.tvVerifyGst.setOnClickListener {
                itemClickListener(
                    adapterPosition,
                    binding.tvVerifyGst,binding
                )
            }

            binding.rbGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
              binding.tvErrorUnderWarranty.visibility = GONE
            })

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
            binding.btnUploadInvoice.setOnClickListener{
                itemClickListener(
                    adapterPosition,
                    binding.btnUploadInvoice,binding
                )
            }
            binding.edtSelectInvoiceDate.setOnClickListener{
                itemClickListener(
                    adapterPosition,
                    binding.edtSelectInvoiceDate,binding
                )
            }
            binding.ivInvoiceImage.setOnClickListener {
                itemClickListener(
                    adapterPosition,
                    binding.ivInvoiceImage,binding
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

        }
    }
}