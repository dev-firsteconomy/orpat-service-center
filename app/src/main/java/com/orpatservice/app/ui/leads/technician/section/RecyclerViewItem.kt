package com.orpatservice.app.ui.leads.technician.section

import com.orpatservice.app.data.model.requests_leads.Enquiry
import com.orpatservice.app.data.model.requests_leads.WarrantryPart
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.TaskEnquiry

open class RecyclerViewItem

class SectionItem(val title: String) : RecyclerViewItem()
class ContentItem(val name: String, val number: Int) : RecyclerViewItem()


/*class SectionItem(val title: String) : RecyclerViewItem()
class ContentItem(val name: String, val number: ArrayList<Enquiry>) : RecyclerViewItem()*/
