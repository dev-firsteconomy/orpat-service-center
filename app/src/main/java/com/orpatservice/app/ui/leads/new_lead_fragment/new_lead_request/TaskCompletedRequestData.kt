package com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request

import android.os.Parcel
import android.os.Parcelable
import com.orpatservice.app.data.model.TechnicianData
import com.orpatservice.app.data.model.requests_leads.Enquiry
import com.orpatservice.app.data.model.requests_leads.LeadData

class TaskCompletedRequestData() : Parcelable {
    var id: Int? = null
    var name: String? = null
    var email: String? = null
    var mobile: String? = null
    var pincode: String? = null
    var address1: String? = null
    var status: String? = null
    var city: String? = null
    var state: String? = null
    var created_at: String? = null
    var enquiries: ArrayList<TaskEnquiry> = arrayListOf()
   /* // var pending_lead_enquiries: String? = null
    var pending_lead_enqury_detail_count: String? = null
    var service_center_assigned_at: String? = null
    var in_warranty_enquiries_count: String? = null
    var technician_assigned_at: String? = null

    var technician: TechnicianData? = null
    var lead_cancelled_reason: String? = null
   */

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        name = parcel.readString()
        email = parcel.readString()
        mobile = parcel.readString()
        pincode = parcel.readString()
        address1 = parcel.readString()
        status = parcel.readString()
        city = parcel.readString()
        state = parcel.readString()
        created_at = parcel.readString()
        parcel.readTypedList(enquiries, TaskEnquiry.CREATOR)
       /* pending_lead_enqury_detail_count = parcel.readString()
        in_warranty_enquiries_count = parcel.readString()
        //   pending_lead_enquiries = parcel.readString()
        service_center_assigned_at = parcel.readString()
        technician_assigned_at = parcel.readString()

        technician = parcel.readParcelable(TechnicianData::class.java.classLoader)
        lead_cancelled_reason = parcel.readString()
        parcel.readTypedList(enquiries, Enquiry.CREATOR)*/

        /*arrayListOf<Enquiry>().apply {
            parcel.readList(this, Enquiry::class.java.classLoader)
        }*/

       /* arrayListOf<Enquiry>().apply {
            parcel.readList(this, Enquiry::class.java.classLoader)
        }*/
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(mobile)
        parcel.writeString(pincode)
        parcel.writeString(address1)
        parcel.writeString(status)
        parcel.writeString(city)
        parcel.writeString(state)
        parcel.writeString(created_at)
        parcel.writeTypedList(enquiries)
        /*parcel?.writeString(pending_lead_enqury_detail_count)
        parcel?.writeString(in_warranty_enquiries_count)
        //     parcel?.writeString(pending_lead_enquiries)
        parcel?.writeString(service_center_assigned_at)
        parcel?.writeString(technician_assigned_at)

        parcel?.writeParcelable(technician, flags)
        parcel?.writeString(lead_cancelled_reason)
        parcel?.writeTypedList(enquiries)*/
    }


    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TaskCompletedRequestData> {
        override fun createFromParcel(parcel: Parcel): TaskCompletedRequestData {
            return TaskCompletedRequestData(parcel)
        }

        override fun newArray(size: Int): Array<TaskCompletedRequestData?> {
            return arrayOfNulls(size)
        }
    }
}