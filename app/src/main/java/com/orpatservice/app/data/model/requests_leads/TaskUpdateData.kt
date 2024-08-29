package com.orpatservice.app.data.model.requests_leads

import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.TaskEnquiry

/*class TaskUpdateData(
    val theView: Int,
    val parentText: String,
    val warrantyText: String,
    val count: String,
    val position: Int,
    val pending_count: String,
    val enquiries: ArrayList<TaskEnquiry>,
    val warranty_part_data: ArrayList<WarrantryPart>
)*/


class TaskUpdateData(
    val theView: Int,
    val model_no: String,
    val in_warranty: String,
    val warrantyText: String,
    val count: String,
    val position: Int,
    val pending_count: String,
    val enquiries: ArrayList<TaskEnquiry>,
    val warranty_part_data: ArrayList<WarrantryPart>,
    val warranty_list: WarrantryPart?,
    val update : String?
)


/*class TaskUpdateData(
    val theView: Int,
    val enquiries: ArrayList<TaskEnquiry>,
    val warrantyText: String,
    val count: String,
    val position: Int,
    val pending_count: String,
    val warranty_part_data: ArrayList<WarrantryPart>?
)*/

