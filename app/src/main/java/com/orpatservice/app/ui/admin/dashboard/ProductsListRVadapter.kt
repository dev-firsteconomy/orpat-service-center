package com.orpatservice.app.ui.admin.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.orpatservice.app.data.model.ProductVideoLinkData
import com.orpatservice.app.data.model.ProductVideoLinkDataResponse
import com.orpatservice.app.data.model.VideoProductCategoriesData
import com.orpatservice.app.databinding.ItemCategoryBinding

class ProductsListRVadapter(private val items: List<ProductVideoLinkData>, private val listener: OnItemClickListener ) : RecyclerView.Adapter<ProductsListRVadapter.MyViewHolder>() {

    // ViewHolder class to hold references to the item views
    class MyViewHolder(private val binding: ItemCategoryBinding,val listener: OnItemClickListener) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProductVideoLinkData) {
            // Use binding to access the TextView and set its text
            binding.tvCategoryTitle.text = item.name
            binding.root.setOnClickListener {
            listener.onItemClick(item)
            }
        }
    }

    // Create new views (this is invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding,listener,)
    }

    // Replace the contents of a view (this is invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = items[position]
       holder.bind(currentItem)
    }

    // Return the size of your dataset
    override fun getItemCount(): Int = items.size


    interface OnItemClickListener {
        fun onItemClick(item: ProductVideoLinkData)
    }
}
