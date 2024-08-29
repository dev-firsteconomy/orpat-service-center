package com.orpatservice.app.data.model

import android.os.Parcel
import android.os.Parcelable
import com.orpatservice.app.ui.admin.technician.PincodeData
import com.orpatservice.app.ui.leads.technician.response.TechnicianEnquiry

data class TechnicianData(
    val id : Int=0,
    val first_name: String,
    val last_name: String,
    val email: String,
    val mobile: String,
    val pincode: String,
   // val area: String,
    val status: Int,
    val image: String,
   // var pincodes: ArrayList<PincodeData> = arrayListOf()
    //val location: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
       // parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
     //   parcel.readString().toString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(first_name)
        parcel.writeString(last_name)
        parcel.writeString(email)
        parcel.writeString(mobile)
        parcel.writeString(pincode)
      //  parcel.writeString(area)
        parcel.writeInt(status)
        parcel.writeString(image)
      //  parcel.writeString(location)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TechnicianData> {
        override fun createFromParcel(parcel: Parcel): TechnicianData {
            return TechnicianData(parcel)
        }

        override fun newArray(size: Int): Array<TechnicianData?> {
            return arrayOfNulls(size)
        }
    }
}
