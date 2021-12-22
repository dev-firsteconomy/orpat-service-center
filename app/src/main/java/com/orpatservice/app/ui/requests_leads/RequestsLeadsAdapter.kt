package com.orpatservice.app.ui.requests_leads

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.orpatservice.app.databinding.ItemRequestLeadCardBinding
import com.orpatservice.app.ui.data.model.requests_leads.LeadData

/**
 * Created by Ajay Yadav on 22/12/21.
 */
class RequestsLeadsAdapter constructor(
    private val leadDataArrayList: ArrayList<LeadData>,
    private val itemClickListener: (Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemRequestLeadCardBinding =
            ItemRequestLeadCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RequestLeadViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RequestLeadViewHolder -> {
                holder.onBind(leadDataArrayList[position], itemClickListener)
            }
        }
    }

    override fun getItemCount(): Int {
        return leadDataArrayList.size
    }

    class RequestLeadViewHolder(private val binding: ItemRequestLeadCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(leadData: LeadData, itemClickListener:(Int)->Unit) {
            binding.btnViewDetails.setOnClickListener{ itemClickListener(adapterPosition) }

            binding.tvRequestId.text = leadData.id.toString()
            binding.tvRequestCustomerName.text = leadData.name
            (leadData.address + " - " + leadData.pincode).also { binding.tvRequestLocation.text = it }
            binding.tvRequestStatus.text = leadData.status
            binding.tvRequestDateTime.text = leadData.createdAt
        }
    }
}