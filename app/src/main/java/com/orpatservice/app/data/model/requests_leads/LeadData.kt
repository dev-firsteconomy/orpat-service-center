package com.orpatservice.app.data.model.requests_leads

import android.os.Parcel
import android.os.Parcelable
import com.orpatservice.app.data.model.TechnicianData

/**
 * Created by Ajay Yadav on 22/12/21.
 */
class LeadData() : Parcelable {
    var id: Int? = null
    var is_open: String? = null
    var complain_id: String? = null
    var name: String? = null
    var email: String? = null
    var mobile: String? = null
    var landmark: String? = null
    var pincode: String? = null
    var address1: String? = null
    var address2: String? = null
    var latitude: String? = null
    var longitude: String? = null
    var status: String? = null
    var city: String? = null
    var state: String? = null
    var timer: String? = null
    var color_code: String? = null
   // var pending_lead_enquiries: String? = null
    var pending_lead_enqury_detail_count: String? = null
    var service_center_assigned_at: String? = null
    var in_warranty_enquiries_count: String? = null
    var technician_assigned_at: String? = null
    var created_at: String? = null
    var technician: TechnicianData? = null
    var lead_cancelled_reason: String? = null
    var pending_parts_verification_status_count: String? = null
    var enquiries: ArrayList<Enquiry> = arrayListOf()

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        is_open = parcel.readString()
        complain_id = parcel.readString()
        name = parcel.readString()
        email = parcel.readString()
        mobile = parcel.readString()
        landmark = parcel.readString()
        pincode = parcel.readString()
        address1 = parcel.readString()
        address2 = parcel.readString()
        latitude = parcel.readString()
        longitude = parcel.readString()
        status = parcel.readString()
        city = parcel.readString()
        state = parcel.readString()
        timer = parcel.readString()
        color_code = parcel.readString()
        pending_lead_enqury_detail_count = parcel.readString()
        in_warranty_enquiries_count = parcel.readString()
     //   pending_lead_enquiries = parcel.readString()
        service_center_assigned_at = parcel.readString()
        technician_assigned_at = parcel.readString()
        created_at = parcel.readString()
        technician = parcel.readParcelable(TechnicianData::class.java.classLoader)
        lead_cancelled_reason = parcel.readString()
        pending_parts_verification_status_count = parcel.readString()
        parcel.readTypedList(enquiries,Enquiry.CREATOR)

        /*arrayListOf<Enquiry>().apply {
            parcel.readList(this, Enquiry::class.java.classLoader)
        }*/
    }

    override fun writeToParcel(parcel: Parcel?, flags: Int) {
        parcel?.writeValue(id)
        parcel?.writeString(is_open)
        parcel?.writeString(complain_id)
        parcel?.writeString(name)
        parcel?.writeString(email)
        parcel?.writeString(mobile)
        parcel?.writeString(landmark)
        parcel?.writeString(pincode)
        parcel?.writeString(address1)
        parcel?.writeString(address2)
        parcel?.writeString(latitude)
        parcel?.writeString(longitude)
        parcel?.writeString(status)
        parcel?.writeString(city)
        parcel?.writeString(state)
        parcel?.writeString(timer)
        parcel?.writeString(color_code)
        parcel?.writeString(pending_lead_enqury_detail_count)
        parcel?.writeString(in_warranty_enquiries_count)
   //     parcel?.writeString(pending_lead_enquiries)
        parcel?.writeString(service_center_assigned_at)
        parcel?.writeString(technician_assigned_at)
        parcel?.writeString(created_at)
        parcel?.writeParcelable(technician, flags)
        parcel?.writeString(lead_cancelled_reason)
        parcel?.writeString(pending_parts_verification_status_count)
        parcel?.writeTypedList(enquiries)
    }


    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LeadData> {
        override fun createFromParcel(parcel: Parcel): LeadData {
            return LeadData(parcel)
        }

        override fun newArray(size: Int): Array<LeadData?> {
            return arrayOfNulls(size)
        }
    }
}