package com.orpatservice.app.ui.leads.technician.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.orpatservice.app.R
import com.orpatservice.app.databinding.ItemTechnicianComplaintBinding
import com.orpatservice.app.ui.leads.technician.response.TechnicianEnquiry
import com.orpatservice.app.ui.leads.technician.response.TechnicianEnquiryImage
import com.orpatservice.app.ui.leads.technician.response.TechnicianLeadData
import kotlin.collections.ArrayList

class TechnicianCustomerDetailsAdapter(
    private val enquiryArrayList: ArrayList<TechnicianEnquiry>,
    private val leadDataArrayList: TechnicianLeadData,
    private val itemClickListener: (Int, View, ItemTechnicianComplaintBinding, ArrayList<TechnicianEnquiryImage>) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemTechnicianComplaintBinding =
            ItemTechnicianComplaintBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ComplaintViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ComplaintViewHolder -> {
                holder.onBind(enquiryArrayList[position].lead_enquiry_images,enquiryArrayList.count(),enquiryArrayList[position],leadDataArrayList, itemClickListener)
            }
        }
    }

    override fun getItemCount(): Int {
        return enquiryArrayList.size
    }

    class ComplaintViewHolder(val binding: ItemTechnicianComplaintBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(
            enquiryImage: ArrayList<TechnicianEnquiryImage>,
            tatalCount: Int,
            enquiry: TechnicianEnquiry,
            leadData: TechnicianLeadData,
            itemClickListener: (Int, View, ItemTechnicianComplaintBinding, ArrayList<TechnicianEnquiryImage>) -> Unit
        ) {

            //yt
            if(!enquiry.installation_link.isNullOrEmpty()||!enquiry.service_link.isNullOrEmpty()){
                binding.ivYt.visibility =  VISIBLE
                binding.tvInstallation.visibility =  VISIBLE

            }else{
                binding.ivYt.visibility =  GONE
                binding.tvInstallation.visibility =  GONE
            }
            if(leadData.service_request_type.equals("installation",true)){
                binding.btnUploadImage.setText("Upload Installed Product Image")
            }else{
                binding.btnUploadImage.setText("Upload Part Image")

            }



            if(enquiry.technician_scan_status == 1){
                binding.liNoScannerPart.visibility = VISIBLE
                binding.liScannerPart.visibility = GONE
            }else{
                binding.liNoScannerPart.visibility = GONE
                binding.liScannerPart.visibility = VISIBLE
            }

            if(enquiry.technician_detail_status == 1 ){
                binding.btnTechnicianUpdate.visibility = View.GONE
                binding.btnTechnicianHideUpdate.visibility = View.VISIBLE

               // binding.liNoScannerPart.visibility = VISIBLE
               // binding.liScannerPart.visibility = GONE


                binding.btnUploadImage.setFocusable(false);
                binding.btnUploadImage.setEnabled(false);
                binding.btnUploadImage.setCursorVisible(false);
                binding.btnUploadImage.setKeyListener(null);
            }else{
                binding.btnTechnicianUpdate.visibility = View.VISIBLE
                binding.btnTechnicianHideUpdate.visibility = View.GONE

                //binding.liNoScannerPart.visibility = GONE
               // binding.liScannerPart.visibility = VISIBLE
            }
            //if(enquiry.purchase_at != null) {
                if (!enquiry.warranty_parts.isEmpty()) {
                    binding.tvServiceableWarrantyParts.visibility = VISIBLE
                } else {
                    binding.tvServiceableWarrantyParts.visibility = GONE
                }
            /*}else{
                binding.tvServiceableWarrantyParts.visibility = GONE
            }*/

            binding.tvDescriptionValue.text = enquiry.customer_discription
            binding.tvModelNameValue.text = enquiry.model_no
            binding.tvServiceCenterValue.text = enquiry.service_center_discription
            binding.tvCustomerIssueValue.text = enquiry.complaint_preset

            binding.tvDescriptionValueScan.text = enquiry.customer_discription
            binding.tvModelNameValueScan.text = enquiry.model_no
            binding.tvServiceCenterValueScan.text = enquiry.service_center_discription
            binding.tvComplaintPresetValueScan.text = enquiry.complaint_preset

            if(!enquiry.lead_enquiry_images.isEmpty()) {
                binding.tvCountImage.visibility = VISIBLE
                binding.tvCountImage.text = "+"+" "+""+enquiry.lead_enquiry_images.count().toString()
            }else{
                binding.tvCountImage.visibility = GONE
            }
             enquiryImage.forEach{
                 Glide.with(binding.ivUploadImage)
                     .load(it.image)
                     // .diskCacheStrategy(DiskCacheStrategy.ALL)
                     //.circleCrop() // .error(R.drawable.active_dot)
                     .placeholder(R.drawable.ic_no_invoice)
                     .into(binding.ivUploadImage)
             }

            Glide.with(binding.ivInvoiceImage)
                .load(enquiry.invoice_url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                //.circleCrop() // .error(R.drawable.active_dot)
                .placeholder(R.drawable.ic_no_invoice)
                .into(binding.ivInvoiceImage)

            Glide.with(binding.ivQrCodeImage)
                .load(enquiry.dummy_barcode)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                //.circleCrop() // .error(R.drawable.active_dot)
                .placeholder(R.color.gray)
                .into(binding.ivQrCodeImage)


            binding.tvQrCodeNumber.text = enquiry.scanned_barcode

            binding.btnScanQrcode.setOnClickListener {
                itemClickListener(
                    adapterPosition,
                    binding.btnScanQrcode,binding,
                    enquiryImage
                )
            }

            binding.tvServiceableWarrantyParts.setOnClickListener {
                itemClickListener(
                    adapterPosition,
                    binding.tvServiceableWarrantyParts,binding,
                    enquiryImage
                )
            }

            binding.btnCancelTask.setOnClickListener {
                itemClickListener(
                    adapterPosition,
                    binding.btnCancelTask,binding,
                    enquiryImage
                )
            }

            binding.ivInvoiceImage.setOnClickListener {
                //binding.ivQrCodeImage.visibility = GONE
                //binding.scannerView.visibility = VISIBLE
                itemClickListener(
                    adapterPosition,
                    binding.ivInvoiceImage,binding,
                    enquiryImage
                )
            }
            binding.btnUploadImage.setOnClickListener{
                itemClickListener(
                    adapterPosition,
                    binding.btnUploadImage,binding,
                    enquiryImage
                )
            }
            binding.ivUploadImage.setOnClickListener{
                itemClickListener(
                    adapterPosition,
                    binding.ivUploadImage,binding,
                    enquiryImage
                )
            }
          /*  binding.btnUploadInvoice.setOnClickListener{
                itemClickListener(
                    adapterPosition,
                    binding.btnUploadInvoice,binding
                )
            }*/

            binding.btnTechnicianUpdate.setOnClickListener{
                    itemClickListener(
                        adapterPosition,
                        binding.btnTechnicianUpdate, binding,
                        enquiryImage

                    )
            }
            binding.tvCountImage.setOnClickListener {
                itemClickListener(
                    adapterPosition,
                    binding.tvCountImage, binding,
                    enquiryImage

                )
            }
            binding.tvInstallation.setOnClickListener {
                itemClickListener(
                    adapterPosition,
                    binding.tvInstallation, binding,
                    enquiryImage

                )
            }
            binding.ivYt.setOnClickListener {
                itemClickListener(
                    adapterPosition,
                    binding.ivYt, binding,
                    enquiryImage

                )
            }


            val posi = adapterPosition+1

            binding.tvTask.setText("Task"+" "+ posi+"/"+tatalCount)
            binding.tvTaskScan.setText("Task"+" "+ posi+"/"+tatalCount)

            binding.ivQrCodeImage.setOnClickListener {
                itemClickListener(
                    adapterPosition,
                    binding.ivQrCodeImage,binding,
                    enquiryImage
                )
            }
            binding.btnScanQr.setOnClickListener {
                itemClickListener(
                    adapterPosition,
                    binding.btnScanQr,binding,
                    enquiryImage
                )
            }
        }
    }
}