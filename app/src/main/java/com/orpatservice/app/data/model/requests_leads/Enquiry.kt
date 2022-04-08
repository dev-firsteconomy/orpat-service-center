package com.orpatservice.app.data.model.requests_leads

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Ajay Yadav on 08/01/22.
 */
class Enquiry() : Parcelable {
    var id: Int? = null
  //  var model_no: String? = null
    var invoice_url: String? = null
    var scanned_barcode: String? = null
   // var qr_image: String? = null
    var purchase_at: String? = null
    var nature_pf_complain: String? = null
    var in_warranty: String? = null
    var status: Boolean = false
    var customer_discription: String? = null
    var dummy_barcode: String? = null
    var model_no: String? = null
    var detail_status: Int? = null


    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
      //  model_no = parcel.readString()
        invoice_url = parcel.readString()
        scanned_barcode = parcel.readString()
     //   qr_image = parcel.readString()
        purchase_at = parcel.readString()
        nature_pf_complain = parcel.readString()
        in_warranty = parcel.readString()
        status = parcel.readValue(Boolean::class.java.classLoader) as Boolean
        customer_discription = parcel.readString()
        dummy_barcode = parcel.readString()
        model_no = parcel.readString()
        detail_status = parcel.readValue(Int::class.java.classLoader) as? Int
    }


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeValue(id)
       // dest?.writeString(model_no)
        dest?.writeString(invoice_url)
        dest?.writeString(scanned_barcode)
      //  dest?.writeString(qr_image)
        dest?.writeString(purchase_at)
        dest?.writeString(nature_pf_complain)
        dest?.writeString(in_warranty)
        dest?.writeValue(status)
        dest?.writeString(customer_discription)
        dest?.writeString(dummy_barcode)
        dest?.writeString(model_no)
        dest?.writeValue(detail_status)
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
