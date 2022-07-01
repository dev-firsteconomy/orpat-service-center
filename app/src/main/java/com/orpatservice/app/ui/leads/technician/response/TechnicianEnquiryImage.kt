package com.orpatservice.app.ui.leads.technician.response

import android.os.Parcel
import android.os.Parcelable

class TechnicianEnquiryImage() : Parcelable {
    var id: Int? = null
    var lead_id: String? = null
    var lead_enquiry_id: String? = null
    var image: String? = null
    var status: String? = null
    var created_at: String? = null
    var updated_at: String? = null


    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        lead_id = parcel.readString()
        lead_enquiry_id = parcel.readString()
        image = parcel.readString()
        status = parcel.readString()
        created_at = parcel.readString()
        updated_at = parcel.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeValue(id)
        dest?.writeString(lead_id)
        dest?.writeString(lead_enquiry_id)
        dest?.writeString(image)
        dest?.writeString(status)
        dest?.writeString(created_at)
        dest?.writeString(updated_at)

    }

    companion object CREATOR : Parcelable.Creator<TechnicianEnquiryImage> {
        override fun createFromParcel(parcel: Parcel): TechnicianEnquiryImage {
            return TechnicianEnquiryImage(parcel)
        }

        override fun newArray(size: Int): Array<TechnicianEnquiryImage?> {
            return arrayOfNulls(size)
        }
    }
}
