package com.orpatservice.app.data.model.requests_leads

import android.os.Parcel
import android.os.Parcelable
import com.orpatservice.app.ui.leads.technician.response.TechnicianEnquiryImage

class LeadEnquiryImage() : Parcelable {
    var id: Int? = null
    var image: String? = null
    var status: String? = null
    var part: String? = null


    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        image = parcel.readString()
        status = parcel.readString()
        part = parcel.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeValue(id)
        dest?.writeString(image)
        dest?.writeString(status)
        dest?.writeString(part)

    }

    companion object CREATOR : Parcelable.Creator<LeadEnquiryImage> {
        override fun createFromParcel(parcel: Parcel): LeadEnquiryImage {
            return LeadEnquiryImage(parcel)
        }

        override fun newArray(size: Int): Array<LeadEnquiryImage?> {
            return arrayOfNulls(size)
        }
    }
}
