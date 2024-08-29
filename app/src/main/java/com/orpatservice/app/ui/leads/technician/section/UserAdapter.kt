package com.orpatservice.app.ui.leads.technician.section

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.orpatservice.app.R


const val VIEW_TYPE_SECTION = 1
const val VIEW_TYPE_ITEM = 2

class UserAdapter(dataSet: ArrayList<RecyclerViewItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data = dataSet


    override fun getItemViewType(position: Int): Int {
        if (data[position] is SectionItem) {
            return VIEW_TYPE_SECTION
        }
        return VIEW_TYPE_ITEM
    }

    override fun getItemCount(): Int {
        println("datadatadata"+data)
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_SECTION) {
            return SectionViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_complete_task, parent, false)
            )
        }
        return ContentViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.under_warranty_parts_adapter, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = data[position]
        if (holder is SectionViewHolder && item is SectionItem) {
            holder.bind(item)
        }
        if (holder is ContentViewHolder && item is ContentItem) {
            holder.bind(item)
        }
    }

    internal inner class SectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.tv_task_completed_model_name_value) as TextView

        fun bind(item: SectionItem) {
            item.title.forEach {
                //println("item.title[adapterPosition].model_no"+it.model_no)
                //textView.text = it.model_no
            }


            //itemView.text_section.text = item.title
        }
    }

    internal inner class ContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.tv_warranty_parts) as TextView
        fun bind(item: ContentItem) {

            textView.text = item.name
          //  itemView.text_number.text = item.number.toString()
        }
    }
}