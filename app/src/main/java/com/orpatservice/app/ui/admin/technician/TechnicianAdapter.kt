package com.orpatservice.app.ui.admin.technician

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.orpatservice.app.R
import com.orpatservice.app.base.Callback
import com.orpatservice.app.databinding.ItemTechnicianBinding
import com.orpatservice.app.ui.data.model.TechnicianData
import com.orpatservice.app.utils.Constants

class TechnicianAdapter(private val techList: ArrayList<TechnicianData>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var callback: Callback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemTechnicianBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_technician, parent, false

        )

        return TechnicianViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TechnicianViewHolder -> {
                holder.bind(techList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return techList.size
    }

    inner class TechnicianViewHolder(private val binding: ItemTechnicianBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.tvEdit.setOnClickListener(this)
            binding.ivCall.setOnClickListener(this)
        }

        fun bind(technicianData: TechnicianData) {
            Glide.with(binding.ivProfile)
                .load(technicianData.image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .circleCrop() // .error(R.drawable.active_dot)
                .placeholder(R.drawable.avtar)
                .into(binding.ivProfile)

            binding.tvTechName.text = technicianData.first_name + " " + technicianData.last_name
            binding.tvLocation.text = technicianData.pincode
            if (technicianData.status == 1) {
                binding.tvIsAvailable.text = Constants.AVAILABLE

            } else {
                binding.tvIsAvailable.text = Constants.NOT_AVILABLE
                binding.tvIsAvailable.setTextColor(
                    ContextCompat.getColor(
                        binding.tvIsAvailable.context,
                        R.color.red
                    )
                )
            }
            if (adapterPosition == techList.size-1) {
                binding.vDivider.visibility = View.GONE

            }
        }

        override fun onClick(view: View?) {
            view?.let { callback?.onItemClick(it, adapterPosition) }
        }
    }
}