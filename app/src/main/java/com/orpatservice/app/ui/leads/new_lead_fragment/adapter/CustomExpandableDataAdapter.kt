package com.orpatservice.app.ui.leads.new_lead_fragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.orpatservice.app.data.model.requests_leads.WarrantryCondition
import com.orpatservice.app.data.model.requests_leads.WarrantryPart
import com.orpatservice.app.databinding.AdapterServiceableWarrantyChildPartsBinding
import com.orpatservice.app.databinding.AdapterServiceableWarrantyPartsBinding
import com.orpatservice.app.utils.Constants

class CustomExpandableDataAdapter internal constructor(
    private val context: Context,
    private val titleList: ArrayList<WarrantryPart>,
    private val dataList: HashMap<String, List<String>>
) : BaseExpandableListAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private lateinit var groupBinding: AdapterServiceableWarrantyPartsBinding
    private lateinit var itemBinding: AdapterServiceableWarrantyChildPartsBinding

    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
       // return this.dataList[this.titleList[listPosition]]!![expandedListPosition]
        return this.titleList[listPosition].warranty_conditions[expandedListPosition].title.toString()
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
            itemBinding = AdapterServiceableWarrantyChildPartsBinding.inflate(inflater)
            convertView = itemBinding.root
            holder = ItemViewHolder()
            holder.label = itemBinding.tvServiceableWarrantyChildPartsName
            holder.singleline = itemBinding.viewLine
            convertView.tag = holder
        } else {
            holder = convertView.tag as ItemViewHolder
        }
        val expandedListText = getChild(listPosition, expandedListPosition) as String
        holder.label!!.text = "-" + " " + expandedListText
        if(!titleList[listPosition].warranty_conditions.isEmpty()) {
            holder.singleline?.visibility = VISIBLE
        }else{
            holder.singleline?.visibility = GONE
        }
        return convertView
    }

    override fun getChildrenCount(listPosition: Int): Int {
        return this.titleList[listPosition].warranty_conditions.size
    }

    override fun getGroup(listPosition: Int): Any {
        return this.titleList[listPosition].name.toString()
    }

    override fun getGroupCount(): Int {
        return this.titleList.size
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
            groupBinding = AdapterServiceableWarrantyPartsBinding.inflate(inflater)
            convertView = groupBinding.root
            holder = GroupViewHolder()
            holder.label = groupBinding.tvServiceableWarrantyPartsName
            holder.notCoveredCondition = groupBinding.tvNotCoveredCondition
            convertView.tag = holder
        } else {
            holder = convertView.tag as GroupViewHolder
        }
        val listTitle = getGroup(listPosition) as String
        holder.label!!.text = listTitle

        println("titleList[listPosition].isExpanded"+titleList[listPosition].isExpanded)
        if(!titleList[listPosition].warranty_conditions.isEmpty()){
            holder.notCoveredCondition?.visibility = View.VISIBLE

        }else{
            holder.notCoveredCondition?.visibility = View.GONE

        }

        /*if(titleList[listPosition].warranty_conditions.count() == 0){
            down_iv?.visibility = View.GONE
        }else {
            down_iv?.visibility = View.VISIBLE
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
        internal var label: TextView? = null
        internal var singleline: View? = null
    }

    inner class GroupViewHolder {
        internal var label: TextView? = null
        internal var notCoveredCondition: TextView? = null
    }
}