package com.orpatservice.app.ui.leads.customer_detail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.orpatservice.app.data.model.RepairParts
import com.orpatservice.app.databinding.ItemComplaintBinding
import com.orpatservice.app.databinding.ItemPartsBinding

/**
 * Created by Vikas Singh on 09/01/22.
 */
class RepairPartAdapter constructor(
    private val partsArrayList: ArrayList<RepairParts>,
    private val itemClickListener: (Int, View) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemPartsBinding =
            ItemPartsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PartsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PartsViewHolder -> {
                holder.onBind(partsArrayList[position],itemClickListener)
            }
        }
    }

    override fun getItemCount(): Int {
        return partsArrayList.size
    }

    class PartsViewHolder(private val binding: ItemPartsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(repairPart: RepairParts, itemClickListener: (Int, View) -> Unit, ) {
            binding.tvParts.text = repairPart.name

            binding.ivCancel.setOnClickListener {
                itemClickListener(
                    adapterPosition,
                    binding.ivCancel
                )
            }

        }
    }
}