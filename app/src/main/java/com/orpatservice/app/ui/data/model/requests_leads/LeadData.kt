package com.orpatservice.app.ui.data.model.requests_leads

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

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
    var model_no: String? = null
    var invoice_image: String? = null
    var qr_image: String? = null
    var purchase_at: String? = null
    var service_center_assigned_at: String? = null
    var technician_assigned_at: String? = null
    var created_at: String? = null
    var nature_of_complain: String? = null
    var technician: String? = null

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
        model_no = parcel.readString()
        invoice_image = parcel.readString()
        qr_image = parcel.readString()
        purchase_at = parcel.readString()
        service_center_assigned_at = parcel.readString()
        technician_assigned_at = parcel.readString()
        created_at = parcel.readString()
        nature_of_complain = parcel.readString()
        technician = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(mobile)
        parcel.writeString(pincode)
        parcel.writeString(address)
        parcel.writeString(status)
        parcel.writeString(city)
        parcel.writeString(state)
        parcel.writeString(model_no)
        parcel.writeString(invoice_image)
        parcel.writeString(qr_image)
        parcel.writeString(purchase_at)
        parcel.writeString(service_center_assigned_at)
        parcel.writeString(technician_assigned_at)
        parcel.writeString(created_at)
        parcel.writeString(nature_of_complain)
        parcel.writeString(technician)
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