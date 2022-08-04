package com.orpatservice.app.ui.admin.technician

import android.os.Parcel
import android.os.Parcelable
import com.orpatservice.app.data.model.TechnicianData
import com.orpatservice.app.data.model.requests_leads.Enquiry
import com.orpatservice.app.data.model.requests_leads.LeadData

class PincodeData() : Parcelable {
   // var id: Int? = null
    var id: String? = null
    var pincode: String? = null
    var officename: String? = null


    constructor(parcel: Parcel) : this() {
       // id = parcel.readValue(Int::class.java.classLoader) as? Int
        id = parcel.readString()
        pincode = parcel.readString()
        officename = parcel.readString()

    }

    override fun writeToParcel(parcel: Parcel?, flags: Int) {
      //  parcel?.writeValue(id)
        parcel?.writeString(id)
        parcel?.writeString(pincode)
        parcel?.writeString(officename)

    }


    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PincodeData> {
        override fun createFromParcel(parcel: Parcel): PincodeData {
            return PincodeData(parcel)
        }

        override fun newArray(size: Int): Array<PincodeData?> {
            return arrayOfNulls(size)
        }
    }

}