package com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request

import android.os.Parcel
import android.os.Parcelable
import com.orpatservice.app.data.model.TechnicianData
import com.orpatservice.app.data.model.requests_leads.LeadData

/**
 * Created by Ajay Yadav on 22/12/21.
 */
class RequestData() : Parcelable {
    var id: Int? = null
    var first_name: String? = null
    var last_name: String? = null
    var email: String? = null
    var mobile: String? = null
    var pincode: String? = null
    var address1: String? = null
    var status: String? = null
    var city: String? = null
    var state: String? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        first_name = parcel.readString()
        last_name = parcel.readString()
        email = parcel.readString()
        mobile = parcel.readString()
        pincode = parcel.readString()
        address1 = parcel.readString()
        status = parcel.readString()
        city = parcel.readString()
        state = parcel.readString()

    }

    override fun writeToParcel(parcel: Parcel?, flags: Int) {
        parcel?.writeValue(id)
        parcel?.writeString(first_name)
        parcel?.writeString(last_name)
        parcel?.writeString(email)
        parcel?.writeString(mobile)
        parcel?.writeString(pincode)
        parcel?.writeString(address1)
        parcel?.writeString(status)
        parcel?.writeString(city)
        parcel?.writeString(state)

    }


    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RequestData> {
        override fun createFromParcel(parcel: Parcel): RequestData {
            return RequestData(parcel)
        }

        override fun newArray(size: Int): Array<RequestData?> {
            return arrayOfNulls(size)
        }
    }
}