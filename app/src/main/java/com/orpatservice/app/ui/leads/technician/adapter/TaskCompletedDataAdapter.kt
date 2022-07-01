package com.orpatservice.app.ui.leads.technician.adapter

import android.R
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.orpatservice.app.data.model.requests_leads.WarrantryPart
import com.orpatservice.app.databinding.FragmentTaskUpdateBinding
import com.orpatservice.app.databinding.ItemComplaintBinding
import com.orpatservice.app.databinding.ItemCompleteTaskBinding
import com.orpatservice.app.databinding.ItemPartsTaskCompletedBinding
import com.orpatservice.app.ui.leads.new_lead_fragment.adapter.WarrantyPartsAdapter
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.TaskEnquiry


class TaskCompletedDataAdapter internal  constructor(
    private val context: Context,
    private val dataList: List<TaskEnquiry>,
    private val itemClickListener: (Int, View,ItemPartsTaskCompletedBinding, Int, ItemCompleteTaskBinding) -> Unit,
) : BaseExpandableListAdapter(){

    private var warrantryPart: ArrayList<WarrantryPart>? = null
    private lateinit var warrantyPartsAdapter: WarrantyPartsAdapter
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private lateinit var groupBinding: ItemCompleteTaskBinding
    private lateinit var itemBinding: ItemPartsTaskCompletedBinding

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
            itemBinding = ItemPartsTaskCompletedBinding.inflate(inflater)
            convertView = itemBinding.root
            holder = ItemViewHolder()
            holder.warrant_parts = itemBinding.tvWarrantyParts
            convertView.tag = holder
        } else {
            holder = convertView.tag as ItemViewHolder
        }
        //val child = getChild(listPosition, expandedListPosition) as String
        warrantryPart = dataList[listPosition].warranty_parts

        holder.warrant_parts?.text = warrantryPart!![expandedListPosition].name

        if(dataList[listPosition].pending_parts_verification_status_count.equals( "0")){
            itemBinding.btnOtpVarify.isClickable = false
            itemBinding.btnOtpVarify.isEnabled = false
            itemBinding.btnOtpVarify.setText("OTP Verification")
            itemBinding.btnOtpVarify.setTextColor(Color.BLACK)
            itemBinding.btnOtpVarify.setBackgroundColor(Color.GRAY)

        }else{

            itemBinding.btnOtpVarify.setText("OTP Verification")
            itemBinding.btnOtpVarify.setTextColor(Color.WHITE)
            itemBinding.btnOtpVarify.setBackgroundColor(Color.BLACK)

        }

        if (expandedListPosition == getChildrenCount(listPosition)-1) {
            itemBinding.btnOtpVarify.visibility = VISIBLE

        }
        if(expandedListPosition == 0){
            itemBinding.btnOtpVarify.visibility = GONE
        }

        itemBinding.btnOtpVarify.setOnClickListener {
            itemClickListener(
                expandedListPosition,
                itemBinding.btnOtpVarify, itemBinding, listPosition, groupBinding
            )
        }

        return convertView
    }

    override fun getChildrenCount(listPosition: Int): Int {
        // return this.taskCompletedRequestData[this.dataList[listPosition]]!!.size
        return this.dataList[listPosition].warranty_parts.size
    }

    override fun getGroup(listPosition: Int): Any {
        return this.dataList[listPosition]
    }

    override fun getGroupCount(): Int {
        return this.dataList.size
    }

    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
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
            groupBinding = ItemCompleteTaskBinding.inflate(inflater)
            convertView = groupBinding.root
            holder = GroupViewHolder()
            holder.model_number = groupBinding.tvTaskCompletedModelNameValue
            holder.warranty = groupBinding.tvTaskCompletedWarrantyDateYear
            holder.taskCount = groupBinding.tvTask
            holder.rv_warrantyList = groupBinding.rvWarrantyList
            convertView.tag = holder
        } else {
            holder = convertView.tag as GroupViewHolder
        }
        val pos = listPosition+1
        holder.model_number?.setText(dataList[listPosition].model_no)
        holder.warranty?.text = dataList[listPosition].in_warranty as String
        holder.taskCount!!.text = "Task"+" "+ pos+"/"+ dataList.count().toString() as String

        /* println("dataListdataList"+dataList[listPosition].warranty_parts)
         warrantyPartsAdapter = WarrantyPartsAdapter(dataList[listPosition].warranty_parts)
         holder.rv_warrantyList?.setAdapter(warrantyPartsAdapter)*/

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
        internal  var rv_warrantyList : RecyclerView? = null
    }
}

/*inner class ViewHolder(val binding: ItemCompleteTaskBinding) :
    RecyclerView.ViewHolder(binding.root)

override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    *//*val binding: ItemCompleteTaskBinding =
            ItemCompleteTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RequestLeadViewHolder(binding)*//*

        return if (viewType == Constants.PARENT) {
            val rowView: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_complete_task, parent, false)
            GroupViewHolder(rowView)
        } else {
            val rowView: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_parts_task_completed, parent, false)
            ChildViewHolder(rowView)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        *//*when (holder) {
            is RequestLeadViewHolder -> {
                holder.onBind(leadDataArrayList[position], itemClickListener)
            }
        }*//*

        val dataList = leadDataArrayList[position]
        if (dataList.type == Constants.PARENT) {
            holder as GroupViewHolder
            holder.apply {
                tvModelNumber?.text = dataList.model_no
                tvWarranty?.text = dataList.in_warranty
                selectParts?.setOnClickListener {
                    expandOrCollapseParentItem(dataList,position)
                }
            }

        } else {
            holder as ChildViewHolder

            holder.apply {
                val singleService = dataList.warranty_parts.first()
                println("singleService.name "+singleService.name)
                warrantyParts?.text = singleService.name
            }
        }
    }
    override fun getItemViewType(position: Int): Int = leadDataArrayList[position].type

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    private fun expandOrCollapseParentItem(enquiry: Enquiry, position: Int) {
        if (enquiry.isExpanded) {
            collapseParentRow(position)
        } else {
            expandParentRow(position)
        }
    }
    private fun collapseParentRow(position: Int){
        val currentBoardingRow = leadDataArrayList[position]
        val services = currentBoardingRow.warranty_parts
        leadDataArrayList[position].isExpanded = false
        if(leadDataArrayList[position].type==Constants.PARENT){
            services.forEach { _ ->
                leadDataArrayList.removeAt(position + 1)
            }
            notifyDataSetChanged()
        }
    }

    private fun expandParentRow(position: Int) {
        val currentBoardingRow = leadDataArrayList[position]
        val services = currentBoardingRow.warranty_parts
        currentBoardingRow.isExpanded = true
        var nextPosition = position
        if(currentBoardingRow.type ==Constants.PARENT){

            services.forEach { service ->
                val parentModel =  Enquiry()
                    parentModel.type = Constants.CHILD
                val subList : ArrayList<WarrantryPart> = ArrayList()
                subList.add(service)
                parentModel.warranty_parts = subList
                leadDataArrayList.add(++nextPosition,parentModel)
            }
            notifyDataSetChanged()
         }
    }

    override fun getItemCount(): Int {
        return leadDataArrayList.size
    }

    class RequestLeadViewHolder(private val binding: ItemCompleteTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun onBind(
            leadData: Enquiry,
            itemClickListener: (Int, View, ItemComplaintBinding) -> Unit,
        ) {

            binding.tvTaskCompletedModelNameValue.text = leadData.model_no
            binding.tvTaskCompletedWarrantyDateYear.text = leadData.in_warranty

            // on Click of the item take parent card view in our case
            // revert the boolean "expand"

        }
    }

        class GroupViewHolder(row: View) : RecyclerView.ViewHolder(row) {
            val tvModelNumber = row.findViewById(R.id.tv_task_completed_model_name_value) as TextView?
            val tvWarranty = row.findViewById(R.id.tv_task_completed_warranty_date_year) as TextView?
            val selectParts = row.findViewById(R.id.tv_select_change_part) as TextView?
        }

        class ChildViewHolder(row: View) : RecyclerView.ViewHolder(row) {
            val warrantyParts = row.findViewById(R.id.tv_warranty_parts) as TextView?

        }*/
