package com.orpatservice.app.ui.leads.technician.section

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.webkit.RenderProcessGoneDetail
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.orpatservice.app.R
import com.orpatservice.app.base.Callback
import com.orpatservice.app.data.model.requests_leads.Enquiry
import com.orpatservice.app.data.model.requests_leads.LeadData
import com.orpatservice.app.data.model.requests_leads.WarrantryPart
import com.orpatservice.app.databinding.FragmentTaskUpdateBinding
import com.orpatservice.app.databinding.TechnicianTaskUpdateBinding
import com.orpatservice.app.ui.leads.new_lead_fragment.adapter.AssignTechnicianLeadAdapter
import com.orpatservice.app.utils.Constants
import com.orpatservice.app.utils.DividerItemDecorator

class TechnicianTaskAdapter(
    private val context: Context,
    private val techList: ArrayList<Enquiry>,
    private val leadData : LeadData,
    private val itemClickListener: (Int, View,TechnicianTaskUpdateBinding) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var callback: Callback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: TechnicianTaskUpdateBinding =
            TechnicianTaskUpdateBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return TechnicianViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TechnicianViewHolder -> {
                holder.bind(techList[position], context,leadData)
            }
        }
    }

    override fun getItemCount(): Int {
        return techList.size
    }

    inner class TechnicianViewHolder(private val binding: TechnicianTaskUpdateBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        private lateinit var linearLayoutManager: LinearLayoutManager

        init {
            // binding.btnAssignAlltechnician.setOnClickListener(this)
        }

        fun bind(technicianData: Enquiry, context: Context,leadData: LeadData) {

            val pos = position + 1
            val count = techList.count()

            //  println("technicianData.pending_parts_verification_status_count"+technicianData.pending_parts_verification_status_count)
            //  println("technicianData.parts_verification_status"+technicianData.parts_verification_status)
            if (/*technicianData.pending_parts_verification_status_count.equals("0") &&*/ technicianData.parts_verification_status.equals(
                    "1"
                )
            ) {
                binding.tvTaskUpdate.visibility = GONE
                binding.tvHideTaskUpdate.visibility = VISIBLE


                if (technicianData.in_warranty.equals("Yes")) {
                    binding.radiobtnYes.isChecked = true

                    binding.radiobtnNo.isChecked = false
                    binding.radiobtnNo.setFocusable(false);
                    binding.radiobtnNo.setEnabled(false);
                    binding.radiobtnNo.setCursorVisible(false);
                    binding.radiobtnNo.setKeyListener(null);

                } else if (technicianData.in_warranty.equals("No")) {
                    binding.radiobtnNo.isChecked = true

                    binding.radiobtnYes.isChecked = false
                    binding.radiobtnYes.setFocusable(false);
                    binding.radiobtnYes.setEnabled(false);
                    binding.radiobtnYes.setCursorVisible(false);
                    binding.radiobtnYes.setKeyListener(null);

                }
              if(technicianData.selected_sub_complaint_preset !=  null){
                  binding.tvSubComplaintPresetValue.text = technicianData.selected_sub_complaint_preset?.name
                  binding.tvSubComplaintPresetValue.isClickable = false
                  binding.tvSubComplaintPresetValue.isEnabled = false
              }

            } else {
                binding.tvTaskUpdate.visibility = VISIBLE
                binding.tvHideTaskUpdate.visibility = GONE

            }
            /* if(technicianData.pending_parts_verification_status_count.equals("0")){
                binding.tvTaskUpdate.visibility = GONE
                binding.tvHideTaskUpdate.visibility = VISIBLE

            }else{
                binding.tvTaskUpdate.visibility = VISIBLE
                binding.tvHideTaskUpdate.visibility = GONE

            }
            if(technicianData.parts_verification_status.equals("1")){
                binding.tvTaskUpdate.visibility = GONE
                binding.tvHideTaskUpdate.visibility = VISIBLE

            }else{
                binding.tvTaskUpdate.visibility = VISIBLE
                binding.tvHideTaskUpdate.visibility = GONE

            }*/

            println("technicianData.status.toString()"+technicianData.status.toString())
            if(technicianData.status.toString() == "Parts Verification Pending"){
                binding.tvSubComplaintPresetValue.visibility = VISIBLE
            }else{
                binding.tvSubComplaintPresetValue.visibility = GONE
            }


            if (technicianData.is_cancelled.equals("Yes")) {
                binding.tvCancelLead.visibility = VISIBLE
                // binding.tvTaskUpdate.visibility = GONE
                //  binding.tvHideTaskUpdate.visibility = VISIBLE
            } else {
                binding.tvCancelLead.visibility = GONE
                // binding.tvTaskUpdate.visibility = VISIBLE
                //  binding.tvHideTaskUpdate.visibility = GONE
            }

            /*if(technicianData.parts_verification_status.equals("1")){
                binding.tvTaskUpdate.visibility = GONE
                binding.tvHideTaskUpdate.visibility = VISIBLE
            }else{
                binding.tvTaskUpdate.visibility = VISIBLE
                binding.tvHideTaskUpdate.visibility = GONE
            }*/

            binding.tvModelNameValue.text = technicianData?.model_no
            binding.tvWarrantyDateYear.text = technicianData?.in_warranty
            binding.tvTask.text = "Task" + "" + pos + "" + "/" + "" + count
            binding.tvCustomerNameValue.text = leadData.name
            binding.tvDateTimeValue.text = leadData.service_center_assigned_at
            binding.tvInvoiceNumberValue.text = technicianData.invoice_no
            binding.tvInvoiceDateValue.text = technicianData.purchase_at
            Glide.with(binding.ivInvoiceImage)
                .load(technicianData.invoice_url)
                .placeholder(R.drawable.ic_no_invoice)
                .into(binding.ivInvoiceImage)

            binding.tvDescriptionValue.text = technicianData.customer_discription
            binding.tvComplaintPresetValue.text = technicianData.complaint_preset

            binding.tvServiceCenterDescriptionValue.text = technicianData.service_center_discription

            val warrantryPart = WarrantryPartAdapter(
                context,
                adapterPosition,
                technicianData.warranty_parts,
                technicianData.lead_enquiry_images,
                technicianData,
            )
            val dividerItemDecoration: RecyclerView.ItemDecoration =
                DividerItemDecorator(ContextCompat.getDrawable(context, R.drawable.rv_divider))

            linearLayoutManager = LinearLayoutManager(context)
            binding.rvWarrantParts.apply {
                adapter = warrantryPart
                //addItemDecoration(dividerItemDecoration)
                layoutManager = linearLayoutManager
            }

            binding.tvTaskUpdate.setOnClickListener {
                itemClickListener(
                    adapterPosition,
                    binding.tvTaskUpdate, binding
                )
            }

            binding.ivInvoiceImage.setOnClickListener {
                itemClickListener(
                    adapterPosition,
                    binding.ivInvoiceImage, binding
                )
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

            /* binding.tvNoWarrantyTaskUpdate.setOnClickListener {
                    itemClickListener(
                        adapterPosition,
                        binding.tvNoWarrantyTaskUpdate
                    )
                }*/
            binding.radiobtnYes.setOnClickListener {
                binding.liNoWarranty.visibility = GONE
                binding.rvWarrantParts.visibility = VISIBLE

                binding.tvTaskUpdate.visibility = VISIBLE
                binding.tvCancelTaskUpdate.visibility = GONE
                itemClickListener(
                    adapterPosition,
                    binding.radiobtnYes, binding
                )
            }
            binding.radiobtnNo.setOnClickListener {
                binding.liNoWarranty.visibility = GONE
                binding.rvWarrantParts.visibility = GONE
                binding.tvSelectChangePart.visibility = GONE
                binding.tvTaskUpdate.visibility = GONE
                binding.tvCancelTaskUpdate.visibility = VISIBLE

                itemClickListener(
                    adapterPosition,
                    binding.radiobtnNo, binding
                )
            }
            binding.tvCancelTaskUpdate.setOnClickListener {
                itemClickListener(
                    adapterPosition,
                    binding.tvCancelTaskUpdate, binding
                )
            }
            binding.radiobtnChangePartYes.setOnClickListener {
                binding.liNoWarranty.visibility = GONE
                binding.rvWarrantParts.visibility = VISIBLE
                itemClickListener(
                    adapterPosition,
                    binding.radiobtnChangePartYes, binding
                )
            }
            binding.radiobtnChangePartNo.setOnClickListener {
                binding.liNoWarranty.visibility = VISIBLE
                binding.rvWarrantParts.visibility = GONE
                itemClickListener(
                    adapterPosition,
                    binding.radiobtnChangePartNo, binding
                )
                //}
            }
            if(leadData.service_request_type.equals("Installation")){
                binding.tvSubComplaintPresetValue.visibility = GONE
                binding.tvSubComplaintPreset.visibility = GONE
                binding.tvSubComplaintPreset.isClickable = false
                binding.tvUnderWarranty.setText("Installation Done")
            }else{
                binding.tvSubComplaintPresetValue.visibility = VISIBLE
                binding.tvSubComplaintPreset.visibility = VISIBLE
                binding.tvSubComplaintPreset.isClickable = true
                binding.tvUnderWarranty.setText("Under Warranty")
            }
        }

        override fun onClick(view: View?) {
            when (view?.id) {
            }
        }
    }
}