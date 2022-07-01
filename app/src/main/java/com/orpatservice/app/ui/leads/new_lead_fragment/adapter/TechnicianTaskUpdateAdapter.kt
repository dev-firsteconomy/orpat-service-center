package com.orpatservice.app.ui.leads.new_lead_fragment.adapter

import android.R
import android.content.Context
import android.graphics.Color
import android.service.voice.VoiceInteractionSession
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import com.orpatservice.app.data.model.requests_leads.Enquiry
import com.orpatservice.app.data.model.requests_leads.LeadData
import com.orpatservice.app.data.model.requests_leads.WarrantryPart
import com.orpatservice.app.databinding.*
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.RequestData
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.TaskEnquiry


class TechnicianTaskUpdateAdapter  internal  constructor(
    context: Context,
    private val requestData: LeadData,
    private val dataList: List<Enquiry>,
    private val itemClickListener: (Int, View, UnderWarrantyPartsAdapterBinding,Int,FragmentTaskUpdateBinding) -> Unit,
) : BaseExpandableListAdapter(){

    private var warrantryPart: ArrayList<WarrantryPart>? = null
    private val inflater: LayoutInflater = LayoutInflater.from(context)


    private lateinit var groupBinding: FragmentTaskUpdateBinding
    private lateinit var itemBinding: UnderWarrantyPartsAdapterBinding
    private lateinit var rvFooterBinding: RvFooterBinding

    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        // return this.dataList[taskCompletedRequestData[listPosition]].isExpanded
        return this.dataList[listPosition].warranty_parts[expandedListPosition]
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }

    override fun getChildView(
        listPosition: Int,
        expandedListPosition: Int,
        isLastChild: Boolean,
        view: View?,
        parent: ViewGroup
    ): View {
        var convertView = view
        val holder: ItemViewHolder

        if (convertView == null) {
            itemBinding = UnderWarrantyPartsAdapterBinding.inflate(inflater)
            convertView = itemBinding.root
            holder = ItemViewHolder()
            holder.warrant_parts = itemBinding.tvWarrantyParts
            convertView.tag = holder
        } else {
            holder = convertView.tag as ItemViewHolder
        }

       // if(expandedListPosition>0 && expandedListPosition<getChildrenCount(listPosition)-1) {

            warrantryPart = dataList[listPosition].warranty_parts

            holder.warrant_parts?.text = warrantryPart!![expandedListPosition].name

            if(dataList[listPosition].pending_parts_verification_status_count.equals( "0")){
                println("requestData.status"+requestData.status)
                itemBinding.btnUpdate.isClickable = false
                itemBinding.btnUpdate.isEnabled = false
                itemBinding.btnUpdate.setText("Update")
                itemBinding.btnUpdate.setTextColor(Color.BLACK)
                itemBinding.btnUpdate.setBackgroundColor(Color.GRAY)
                //itemBinding.btnHideUpdate.visibility = VISIBLE
               // itemBinding.btnUpdate.visibility = GONE
            }else{
               // itemBinding.btnUpdate.isClickable = true
                itemBinding.btnUpdate.setText("Update")
                itemBinding.btnUpdate.setTextColor(Color.WHITE)
                itemBinding.btnUpdate.setBackgroundColor(Color.BLACK)
              //  itemBinding.btnHideUpdate.visibility = GONE
               // itemBinding.btnUpdate.visibility = VISIBLE
            }

            itemBinding.checkWarrantyParts.setOnClickListener {

                itemClickListener(
                    expandedListPosition,
                    itemBinding.checkWarrantyParts, itemBinding, listPosition, groupBinding
                )
            }
            itemBinding.btnUpdate.setOnClickListener {
                itemClickListener(
                    expandedListPosition,
                    itemBinding.btnUpdate, itemBinding, listPosition, groupBinding
                )
            }

        if (expandedListPosition == getChildrenCount(listPosition)-1) {
            itemBinding.btnUpdate.visibility = View.VISIBLE
           // itemBinding.btnHideUpdate.visibility = View.VISIBLE
        }

        if(expandedListPosition == 0){
            itemBinding.btnUpdate.visibility = View.GONE
            itemBinding.btnHideUpdate.visibility = View.GONE
        }
        if(expandedListPosition == 1){
            itemBinding.btnUpdate.visibility = View.GONE
            itemBinding.btnHideUpdate.visibility = View.GONE
        }
        if(expandedListPosition == 2){
            itemBinding.btnUpdate.visibility = View.GONE
            itemBinding.btnHideUpdate.visibility = View.GONE
        }
        if(expandedListPosition == 3){
            itemBinding.btnUpdate.visibility = View.GONE
        }
        itemBinding.btnHideUpdate.visibility = View.GONE
        if(expandedListPosition == 4){
            itemBinding.btnUpdate.visibility = View.GONE
            itemBinding.btnHideUpdate.visibility = View.GONE
        }
        /*if(groupBinding.radiobtnYes.isChecked){
            itemBinding.liYesWarrantyParts.visibility = VISIBLE
            itemBinding.liNoWarrantyParts.visibility = GONE
        }else{
            itemBinding.liNoWarrantyParts.visibility = VISIBLE
            itemBinding.liYesWarrantyParts.visibility = GONE
        }*/

        return convertView
    }

    override fun getChildrenCount(listPosition: Int): Int {
        return this.dataList[listPosition].warranty_parts.size
    }

    override fun getGroup(listPosition: Int): Any {
        return 1
        //return this.dataList[listPosition]
    }

    override fun getGroupCount(): Int {
        return 1
        //return dataList.size
    }

    override fun getGroupId(listPosition: Int): Long {
        return 1
        //return listPosition.toLong()
    }

    override fun getGroupView(
        listPosition: Int,
        isExpanded: Boolean,
        view: View?,
        parent: ViewGroup
    ): View {
        var convertView = view
        val holder: GroupViewHolder

        if (convertView == null) {
            groupBinding = FragmentTaskUpdateBinding.inflate(inflater)
            convertView = groupBinding.root
            holder = GroupViewHolder()
            holder.model_number = groupBinding.tvModelNameValue
            holder.warranty = groupBinding.tvWarrantyDateYear
            holder.taskCount = groupBinding.tvTask

            val posi = listPosition+1
            val tatalCount = dataList.count().toString()
            holder.taskCount?.setText("Task"+" "+ posi+"/"+tatalCount);
            convertView.tag = holder
        } else {
            holder = convertView.tag as GroupViewHolder
        }
        var pos = listPosition+1
        holder.model_number?.setText(dataList[listPosition].model_no)
        holder.warranty?.text = dataList[listPosition].in_warranty as String


        /*groupBinding.radioBtnChangePart.setOnClickListener {
            itemClickListener(
                listPosition,
                groupBinding.radioBtnChangePart,itemBinding,listPosition,groupBinding)
        }*/

        return convertView
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }

    inner class ItemViewHolder {
        internal var warrant_parts: TextView? = null
    }

    inner class GroupViewHolder {
        internal var model_number: TextView? = null
        internal var warranty: TextView? = null
        internal var taskCount: TextView? = null
    }
}


/*package com.orpatservice.app.ui.leads.technician.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.orpatservice.app.R
import com.orpatservice.app.data.model.requests_leads.RecyclerViewItem
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.TaskEnquiry


class TaskCompletedDataAdapter (
    val context: Context,
    val taskEnquary:  List<TaskEnquiry>) :
    RecyclerView.Adapter< RecyclerView.ViewHolder>() {

     companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEMS = 1
        private const val TYPE_FOOTER = 2
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            TYPE_HEADER -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_complete_task, parent, false)
                HeaderViewHolder(view)
            }

            TYPE_ITEMS -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_parts_task_completed, parent, false)
                ItemViewHolder(view)
            }

            TYPE_FOOTER -> {
                val view = LayoutInflater.from(context).inflate(R.layout.rv_footer, parent, false)
                FooterViewHolder(view)
            }

           else -> throw IllegalArgumentException("Invalid View Type")
        }
    }

    override fun getItemCount(): Int {
        return taskEnquary.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is HeaderViewHolder) {
               // holder.modelNumber.text = data[position]
                holder.in_warranty.text = taskEnquary[position].in_warranty
        }

        if (holder is ItemViewHolder) {

            holder.warrantyParts.text = taskEnquary[position].model_no
        }
        if (holder is FooterViewHolder) {

        }
    }

    override fun getItemViewType(position: Int): Int {
        if(position == 0) {
            return TYPE_HEADER
        }/*else if(position == numberList.size + 2){
            return TYPE_FOOTER
        }*/

        return TYPE_ITEMS
    }

     class HeaderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val modelNumber : TextView = itemView.findViewById(R.id.tv_task_completed_model_name_value)
        val in_warranty : TextView = itemView.findViewById(R.id.tv_task_completed_warranty_date_year)
         fun bind(item: View) {

        }
    }

     class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val warrantyParts = itemView.findViewById<TextView>(R.id.tv_warranty_parts)

         fun bind(item: View) {

        }
    }
     class FooterViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

         fun bind(item: View) {

        }

    }
}*/
