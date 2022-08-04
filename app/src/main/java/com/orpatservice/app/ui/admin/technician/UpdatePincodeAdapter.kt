package com.orpatservice.app.ui.admin.technician

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.orpatservice.app.databinding.AdapterRequestPincodeBinding
import com.orpatservice.app.utils.CommonUtils

class UpdatePincodeAdapter (private val mList: List<PincodeData>,
                            private val pincodeList: List<PincodeData>,
                            private val itemClickListener: (Int, View, AdapterRequestPincodeBinding) -> Unit,
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
                    mList[position],pincodeList,
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
            pincodeList: List<PincodeData>,
            itemClickListener: (Int, View, AdapterRequestPincodeBinding) -> Unit
        ) {

            /*for(i in pincodeList){
                if(i.pincode.equals(list.pincode)){

                    val pincodeData = PinData(
                        i.id.toString(),
                        i.pincode.toString(),
                    )
                    CommonUtils.pincodeData.add(pincodeData)
                }
            }*/

            if (!CommonUtils.pincodeData.isEmpty()) {
                for (i in CommonUtils.pincodeData) {
                    binding.tvPincodeList.text = list.pincode
                    binding.tvPincodeListName.text = list.officename

                    if(i.pincode.equals(list.pincode)){
                        binding.checkId.isChecked = true
                    }
                }
            }else {
               // CommonUtils.pincodeData.clear()

                binding.tvPincodeList.text = list.pincode
                binding.tvPincodeListName.text = list.officename
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