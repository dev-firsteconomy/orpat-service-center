package com.orpatservice.app.ui.leads.new_lead_fragment.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.orpatservice.app.base.Callback
import com.orpatservice.app.data.model.requests_leads.LeadEnquiryImage
import com.orpatservice.app.data.model.requests_leads.WarrantryPart
import com.orpatservice.app.databinding.ItemPartsTaskCompletedBinding
import com.orpatservice.app.ui.leads.technician.response.TechnicianEnquiryImage

class TaskCompletedWarrantyAdapter (

    private val warrantyList: ArrayList<LeadEnquiryImage>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var callback: Callback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemPartsTaskCompletedBinding =
            ItemPartsTaskCompletedBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return TechnicianViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TechnicianViewHolder -> {
                holder.bind(warrantyList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return warrantyList.size
    }

    inner class TechnicianViewHolder(private val binding: ItemPartsTaskCompletedBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            // binding.btnAssignAlltechnician.setOnClickListener(this)
        }

        fun bind(technicianData: LeadEnquiryImage) {

            if(technicianData.status.equals("Approved")) {
                binding.tvWarrantyParts.text =
                    technicianData.part
            }
            /*binding.tvWarrantyParts.setOnClickListener {
                val warrantyPartData = productListData(warrantyList[position].id.toString())
                CommonUtils.productListData.add(warrantyPartData)
            }*/
        }

        override fun onClick(p0: View?) {

        }
    }
}

