package com.orpatservice.app.ui.leads.technician.response

import android.os.Parcel
import android.os.Parcelable
import com.orpatservice.app.data.model.requests_leads.WarrantryPart

class TechnicianEnquiry() : Parcelable {
    var id: Int? = null
    //  var model_no: String? = null
    var invoice_url: String? = null
    var scanned_barcode: String? = null
    // var qr_image: String? = null
    var purchase_at: String? = null
    var complaint_preset: String? = null
    var in_warranty: String? = null
    var status: Boolean = false
    var customer_discription: String? = null
    var service_center_discription: String? = null
    var dummy_barcode: String? = null
    var model_no: String? = null
    var technician_detail_status: Int? = null
    var technician_scan_status: Int? = null
    var warranty_parts: ArrayList<WarrantryPart> = arrayListOf()
    var lead_enquiry_images: ArrayList<TechnicianEnquiryImage> = arrayListOf()


    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        //  model_no = parcel.readString()
        invoice_url = parcel.readString()
        scanned_barcode = parcel.readString()
        //   qr_image = parcel.readString()
        purchase_at = parcel.readString()
        complaint_preset = parcel.readString()
        in_warranty = parcel.readString()
        status = parcel.readValue(Boolean::class.java.classLoader) as Boolean
        customer_discription = parcel.readString()
        service_center_discription = parcel.readString()
        dummy_barcode = parcel.readString()
        model_no = parcel.readString()
        technician_detail_status = parcel.readValue(Int::class.java.classLoader) as? Int
        technician_scan_status = parcel.readValue(Int::class.java.classLoader) as? Int
        parcel.readTypedList(warranty_parts,WarrantryPart.CREATOR)
        parcel.readTypedList(lead_enquiry_images, TechnicianEnquiryImage.CREATOR)
    }


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(id)
        // dest?.writeString(model_no)
        dest.writeString(invoice_url)
        dest.writeString(scanned_barcode)
        //  dest?.writeString(qr_image)
        dest.writeString(purchase_at)
        dest.writeString(complaint_preset)
        dest.writeString(in_warranty)
        dest.writeValue(status)
        dest.writeString(customer_discription)
        dest.writeString(service_center_discription)
        dest.writeString(dummy_barcode)
        dest.writeString(model_no)
        dest.writeValue(technician_detail_status)
        dest.writeValue(technician_scan_status)
        dest.writeTypedList(warranty_parts)
        dest.writeTypedList(lead_enquiry_images)
    }

    companion object CREATOR : Parcelable.Creator<TechnicianEnquiry> {
        override fun createFromParcel(parcel: Parcel): TechnicianEnquiry {
            return TechnicianEnquiry(parcel)
        }

        override fun newArray(size: Int): Array<TechnicianEnquiry?> {
            return arrayOfNulls(size)
        }
    }
}
