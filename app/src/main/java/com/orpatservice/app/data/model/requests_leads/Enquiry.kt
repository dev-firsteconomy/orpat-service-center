package com.orpatservice.app.data.model.requests_leads

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Ajay Yadav on 08/01/22.
 */
class Enquiry() : Parcelable {
    var id: Int? = null
    var model_no: String? = null
    var invoice_image: String? = null
    var qr_image: String? = null
    var purchase_at: String? = null
    var nature_pf_complain: String? = null
    var in_warranty: String? = null
    var status: Boolean = false

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        model_no = parcel.readString()
        invoice_image = parcel.readString()
        qr_image = parcel.readString()
        purchase_at = parcel.readString()
        nature_pf_complain = parcel.readString()
        in_warranty = parcel.readString()
        status = parcel.readValue(Boolean::class.java.classLoader) as Boolean
    }


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeValue(id)
        dest?.writeString(model_no)
        dest?.writeString(invoice_image)
        dest?.writeString(qr_image)
        dest?.writeString(purchase_at)
        dest?.writeString(nature_pf_complain)
        dest?.writeString(in_warranty)
        dest?.writeValue(status)
    }

    companion object CREATOR : Parcelable.Creator<Enquiry> {
        override fun createFromParcel(parcel: Parcel): Enquiry {
            return Enquiry(parcel)
        }

        override fun newArray(size: Int): Array<Enquiry?> {
            return arrayOfNulls(size)
        }
    }
}
