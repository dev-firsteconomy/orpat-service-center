package com.orpatservice.app.ui.admin.technician

import android.os.Parcel
import android.os.Parcelable

class TechnicianList() : Parcelable {
    var id: Int? = null
    var first_name: String? = null
    var last_name: String? = null
    var aadhar_card_no: String? = null
    var aadhar_image: String? = null
    var email: String? = null
    var mobile: String? = null
    var pincode: String? = null
    var image: String? = null
    var status: String? = null
    var passcode: String? = null
    var active_leads_count: String? = null
    var pincodes: ArrayList<PincodeData> = arrayListOf()

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        first_name = parcel.readString()
        last_name = parcel.readString()
        aadhar_card_no = parcel.readString()
        aadhar_image = parcel.readString()
        email = parcel.readString()
        mobile = parcel.readString()
        pincode = parcel.readString()
        image = parcel.readString()
        status = parcel.readString()
        passcode = parcel.readString()
        active_leads_count = parcel.readString()
        parcel.readTypedList(pincodes, PincodeData.CREATOR)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(first_name)
        parcel.writeString(last_name)
        parcel.writeString(aadhar_card_no)
        parcel.writeString(aadhar_image)
        parcel.writeString(email)
        parcel.writeString(mobile)
        parcel.writeString(pincode)
        parcel.writeString(image)
        parcel.writeString(status)
        parcel.writeString(passcode)
        parcel.writeString(active_leads_count)
        parcel.writeTypedList(pincodes)
    }


    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TechnicianList> {
        override fun createFromParcel(parcel: Parcel): TechnicianList {
            return TechnicianList(parcel)
        }

        override fun newArray(size: Int): Array<TechnicianList?> {
            return arrayOfNulls(size)
        }
    }
}