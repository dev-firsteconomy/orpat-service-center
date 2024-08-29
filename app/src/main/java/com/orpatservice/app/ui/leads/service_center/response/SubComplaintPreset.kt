package com.orpatservice.app.ui.leads.service_center.response

import android.os.Parcel
import android.os.Parcelable
import com.orpatservice.app.data.model.requests_leads.WarrantryCondition
import com.orpatservice.app.data.model.requests_leads.WarrantryPart
import com.orpatservice.app.utils.Constants

class SubComplaintPreset() : Parcelable {
    var id: Int? = null
    var name: String? = null
    var is_free_service: String? = null


    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        name = parcel.readString()
        is_free_service = parcel.readString()
    }


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(id)
        dest.writeString(name)
        dest.writeString(is_free_service)
    }

    companion object CREATOR : Parcelable.Creator<SubComplaintPreset> {
        override fun createFromParcel(parcel: Parcel): SubComplaintPreset {
            return SubComplaintPreset(parcel)
        }

        override fun newArray(size: Int): Array<SubComplaintPreset?> {
            return arrayOfNulls(size)
        }
    }
}
