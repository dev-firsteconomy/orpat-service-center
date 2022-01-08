package com.orpatservice.app.data.model.requests_leads

import android.os.Parcel
import android.os.Parcelable
import com.orpatservice.app.data.model.TechnicianData

/**
 * Created by Ajay Yadav on 22/12/21.
 */
class LeadData() : Parcelable {
    var id: Int? = null
    var name: String? = null
    var email: String? = null
    var mobile: String? = null
    var pincode: String? = null
    var address: String? = null
    var status: String? = null
    var city: String? = null
    var state: String? = null
    var service_center_assigned_at: String? = null
    var technician_assigned_at: String? = null
    var created_at: String? = null
    var technician: TechnicianData? = null
    var lead_cancelled_reason: String? = null
    var enquiries: ArrayList<Enquiry> = arrayListOf()

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        name = parcel.readString()
        email = parcel.readString()
        mobile = parcel.readString()
        pincode = parcel.readString()
        address = parcel.readString()
        status = parcel.readString()
        city = parcel.readString()
        state = parcel.readString()
        service_center_assigned_at = parcel.readString()
        technician_assigned_at = parcel.readString()
        created_at = parcel.readString()
        technician = parcel.readParcelable(TechnicianData::class.java.classLoader)
        lead_cancelled_reason = parcel.readString()
        arrayListOf<Enquiry>().apply {
            parcel.readList(this, Enquiry::class.java.classLoader)
        }
    }

    override fun writeToParcel(parcel: Parcel?, flags: Int) {
        parcel?.writeValue(id)
        parcel?.writeString(name)
        parcel?.writeString(email)
        parcel?.writeString(mobile)
        parcel?.writeString(pincode)
        parcel?.writeString(address)
        parcel?.writeString(status)
        parcel?.writeString(city)
        parcel?.writeString(state)
        parcel?.writeString(service_center_assigned_at)
        parcel?.writeString(technician_assigned_at)
        parcel?.writeString(created_at)
        parcel?.writeParcelable(technician, flags)
        parcel?.writeString(lead_cancelled_reason)
        arrayListOf<Enquiry>().apply {
            parcel?.writeTypedList(enquiries)
        }
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