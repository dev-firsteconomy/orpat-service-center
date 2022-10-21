package com.orpatservice.app.utils

object Constants {

    const val MODULE_TYPE = "MODULE_TYPE"
    const val LEAD_DATA= "LEAD_DATA"
    const val LEAD_ID= "LEAD_ID"
    const val TASK_DATA= "TASK_DATA"
    const val UPDATE_DATA= "UPDATE_DATA"
    const val IMAGE_DATA= "IMAGE_DATA"
    const val POSITION= "POSITION"
    const val LEAD_DETAILS= "LEAD_DETAILS"
    const val TECHNICIANS_DATA= "TECHNICIANS_DATA"

    const val LATITUDE= "LEAD_DATA"
    const val LONGITUDE= "LEAD_DATA"

    var REQUEST_TOTAL= ""
    var ASSIGN_TOTAL= ""
    var VERIFY_TOTAL= ""
    var COMPLETE_REQ= ""
    var CANCELLED_REQ= ""
    var TECHNICIAN_TOTAL= ""

    //Constants for login session
    const val TOKEN = "TOKEN"
    const val NO_TOKEN = ""
    const val SERVICE_CENTER_DATA = "SERVICE_CENTER"

    const val MOBILE_NUMBER = "MOBILE_NUMBER"
    const val USER_TYPE = "USER_TYPE"
    const val SERVICE_CENTER = "SERVICE_CENTER"
    const val TECHNICIAN = "TECHNICIAN"

    const val REQUEST = "Requests"
    /*val requestsTabNameArray = arrayOf(
        "New Request",
        "Assign To Technician",
        "Task Completed"
    )*/


    val requestsTabNameArray = arrayOf(
        "New",
        "Assigned",
        "Verify"
    )

   /* val requestsTabNameArray = arrayOf(
        "New",
        "Assigned",
        "Parts",
        "Completed"
    )*/


    val requestsTechnicianTabNameArray = arrayOf(
        "New",
        "Completed Request",
        "Cancelled",
    )

    const val PARENT = 0
    const val CHILD = 1

    const val HISTORY = "History"
    val requestsHistoryTabNameArray = arrayOf(
        "Completed Request",
        "Cancelled Request"
    )

    //Fragment Type
    const val LEAD_TYPE = "LEAD_TYPE"
    const val LEAD_NEW = "LEAD_NEW"
    const val LEAD_ASSIGN_TECHNICIAN = "LEAD_ASSIGN_TECHNICIAN"
    const val LEAD_COMPLETED_REQUEST = "LEAD_COMPLETED_REQUEST"
    const val LEAD_CANCELLED_REQUEST = "LEAD_CANCELLED_REQUEST"

    const val AVAILABLE = "Available"
    const val NOT_AVILABLE = "Not available"

    //Leads status
    const val PENDING = "Pending"
    const val TECHNICIAN_ASSIGNED = "Technician Assined"

    // Technician
    const val IS_NAV = "IS_NAV"
    const val LEADS_ID = "LEADS_ID"
    const val IMAGE_URL = "IMAGE_URL"
    const val SELECTED_PARTS = "SELECTED_PARTS"
    const val TECHNICIAN_ID = "SELECTED_PARTS"
    const val COMPLAINT_ID = "COMPLAINT_ID"
    const val TOTAL_ENQUIRY = "TOTAL_ENQUIRY"

    object ComingFrom {

        val CUSTOMER_DETAILS = "CUSTOMER_DETAILS"
        val DASHBOARD = "DASHBOARD"

    }

}