package com.orpatservice.app.ui.leads.new_lead_fragment.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.orpatservice.app.data.model.requests_leads.WarrantryPart
import com.orpatservice.app.databinding.ItemPartsTaskCompletedBinding
import com.orpatservice.app.databinding.ItemRequestAssignedTechnicianBinding
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.RequestData
import java.util.ArrayList

class WarrantyPartsAdapter constructor(
    private val leadDataArrayList: ArrayList<WarrantryPart>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemPartsTaskCompletedBinding =
            ItemPartsTaskCompletedBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return RequestLeadViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RequestLeadViewHolder -> {
                holder.onBind(leadDataArrayList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return leadDataArrayList.size
    }

    class RequestLeadViewHolder(private val binding: ItemPartsTaskCompletedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun onBind(
            leadData: WarrantryPart

        ) {
            println("leadDataleadDataleadData"+leadData.name)
            binding.tvWarrantyParts.text = leadData.name

        }
    }
}