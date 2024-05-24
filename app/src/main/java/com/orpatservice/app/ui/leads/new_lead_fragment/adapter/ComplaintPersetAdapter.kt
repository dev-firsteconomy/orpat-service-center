package com.orpatservice.app.ui.leads.new_lead_fragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.orpatservice.app.data.model.requests_leads.Enquiry
import com.orpatservice.app.databinding.AdapterSubComplaintPresetBinding
import com.orpatservice.app.ui.leads.service_center.response.SubComplaintPreset

class ComplaintPersetAdapter(private val mList: List<SubComplaintPreset>,
                             private val itemClickListener: (Int, View, AdapterSubComplaintPresetBinding) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val binding: AdapterSubComplaintPresetBinding =
            AdapterSubComplaintPresetBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return ComplaintViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ComplaintViewHolder -> {
                holder.onBind(
                    mList[position],
                    /*    complaintData[position],*/
                    itemClickListener
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ComplaintViewHolder(private val binding: AdapterSubComplaintPresetBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(
            list: SubComplaintPreset,
            /*complaintDta:ComplaintPresetData,*/
            itemClickListener: (Int, View, AdapterSubComplaintPresetBinding) -> Unit
        ) {
            binding.tvSubComplaintName.text = list.name
            binding.tvSubComplaintName.setOnClickListener {
                itemClickListener(
                    adapterPosition,
                    binding.tvSubComplaintName,binding
                )
            }
            /*  println("complaintDta.complaint_preset.name"+complaintDta.id)
              binding.tvComplaintName.text = complaintDta.name
              binding.tvComplaintName.setOnClickListener {
                  itemClickListener(
                      adapterPosition,
                      binding.tvComplaintName,binding
                  )
              }*/
        }
    }
}