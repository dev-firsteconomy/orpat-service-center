package com.orpatservice.app.ui.leads.customer_detail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.orpatservice.app.data.model.requests_leads.WarrantryPart
import com.orpatservice.app.databinding.AdapterServiceableWarrantyPartsBinding

class ServiceableWarrantryPartAdapter(private val mList: List<WarrantryPart>,
                                      /*private val itemClickListener: (Int, View, AdapterServiceableWarrantyPartsBinding) -> Unit,*/
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val binding: AdapterServiceableWarrantyPartsBinding =
            AdapterServiceableWarrantyPartsBinding.inflate(
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
                    //itemClickListener
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ComplaintViewHolder(private val binding: AdapterServiceableWarrantyPartsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(
            list: WarrantryPart,
            //itemClickListener: (Int, View, AdapterServiceableWarrantyPartsBinding) -> Unit
        ) {
            binding.tvServiceableWarrantyPartsName.text = list.name
        }
    }
}