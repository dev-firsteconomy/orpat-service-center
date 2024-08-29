package com.orpatservice.app.ui.admin.technician

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.orpatservice.app.databinding.AdapterRequestPincodeBinding
import com.orpatservice.app.utils.CommonUtils

class RequestsPincodeAdapter (private val mList: List<PincodeData>,
                              private val itemClickListener: (Int, View,AdapterRequestPincodeBinding) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val binding: AdapterRequestPincodeBinding =
            AdapterRequestPincodeBinding.inflate(
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
                    itemClickListener
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ComplaintViewHolder(private val binding: AdapterRequestPincodeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(
            list: PincodeData,
            itemClickListener: (Int, View,AdapterRequestPincodeBinding) -> Unit
        ) {

            if (!CommonUtils.pincodeData.isEmpty()) {
                for (i in CommonUtils.pincodeData) {
                    binding.tvPincodeList.text = list.pincode
                    binding.tvPincodeListName.text = list.officename

                    println("i.pincode"+i.pincode)
                    if(i.pincode.equals(list.pincode)){
                        binding.checkId.isChecked = true
                    }
                }
            }else {
                binding.tvPincodeList.text = list.pincode
                binding.tvPincodeListName.text = list.officename
            }
            if(list.pincode.isNullOrEmpty()){
                binding.checkId.isChecked = true
            }

            binding.tvPincodeList.setOnClickListener {
                itemClickListener(
                    adapterPosition,
                    binding.tvPincodeList,binding
                )
            }
            binding.checkId.setOnClickListener {
                itemClickListener(
                    adapterPosition,
                    binding.checkId,binding
                )
            }
        }
    }
}