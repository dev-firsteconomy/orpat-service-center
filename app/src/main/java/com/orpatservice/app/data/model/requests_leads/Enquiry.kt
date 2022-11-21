package com.orpatservice.app.data.model.requests_leads

import android.os.Parcel
import android.os.Parcelable
import com.orpatservice.app.ui.leads.technician.response.TechnicianEnquiryImage
import com.orpatservice.app.utils.Constants

/**
 * Created by Ajay Yadav on 08/01/22.
 */
class Enquiry() : Parcelable {
    var id: Int? = null
    var scanned_barcode: String? = null
    var model_no: String? = null
    var invoice_url: String? = null
    var invoice_no: String? = null

   // var qr_image: String? = null
    var purchase_at: String? = null
    var complaint_preset: String? = null
    var in_warranty: String? = null
    var buyer_name: String? = null
    var seller_name: String? = null
    var seller_gst_no: String? = null
    var seller_trade_name: String? = null
    var is_cancelled: String? = null
    var status: Boolean = false
    var customer_discription: String? = null
    var service_center_discription: String? = null
    var dummy_barcode: String? = null
   // var model_no: String? = null
    var detail_status: Int? = null
    var technician_detail_status: String? = null
    var parts_verification_status: String? = null
    var pending_lead_enqury_detail_count: String? = null
    var in_warranty_enquiries_count: String? = null
    var pending_technician_detail_count: String? = null
    var pending_parts_verification_status_count: String? = null
    var warranty_parts: ArrayList<WarrantryPart> = arrayListOf()
    var lead_enquiry_images: ArrayList<LeadEnquiryImage> = arrayListOf()


    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        scanned_barcode = parcel.readString()
        model_no = parcel.readString()
        invoice_url = parcel.readString()
        invoice_no = parcel.readString()
     //   qr_image = parcel.readString()
        purchase_at = parcel.readString()
        complaint_preset = parcel.readString()
        in_warranty = parcel.readString()
        buyer_name = parcel.readString()
        seller_name = parcel.readString()
        seller_gst_no = parcel.readString()
        seller_trade_name = parcel.readString()
        is_cancelled = parcel.readString()
        status = parcel.readValue(Boolean::class.java.classLoader) as Boolean
        customer_discription = parcel.readString()
        service_center_discription = parcel.readString()
        dummy_barcode = parcel.readString()
       // model_no = parcel.readString()
        detail_status = parcel.readValue(Int::class.java.classLoader) as? Int
        technician_detail_status =  parcel.readString()
        parts_verification_status =  parcel.readString()
        pending_lead_enqury_detail_count =  parcel.readString()
        in_warranty_enquiries_count =  parcel.readString()
        pending_technician_detail_count =  parcel.readString()
        pending_parts_verification_status_count = parcel.readString()
        parcel.readTypedList(warranty_parts,WarrantryPart.CREATOR)
        parcel.readTypedList(lead_enquiry_images, LeadEnquiryImage.CREATOR)

    }


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeValue(id)
        dest?.writeString(scanned_barcode)
        dest?.writeString(model_no)
        dest?.writeString(invoice_url)
        dest?.writeString(invoice_no)
      //  dest?.writeString(qr_image)
        dest?.writeString(purchase_at)
        dest?.writeString(complaint_preset)
        dest?.writeString(in_warranty)
        dest?.writeString(buyer_name)
        dest?.writeString(seller_name)
        dest?.writeString(seller_gst_no)
        dest?.writeString(seller_trade_name)
        dest?.writeString(is_cancelled)
        dest?.writeValue(status)
        dest?.writeString(customer_discription)
        dest?.writeString(service_center_discription)
        dest?.writeString(dummy_barcode)
       // dest?.writeString(model_no)
        dest?.writeValue(detail_status)
        dest?.writeString(technician_detail_status)
        dest?.writeString(parts_verification_status)
        dest?.writeString(pending_lead_enqury_detail_count)
        dest?.writeString(in_warranty_enquiries_count)
        dest?.writeString(pending_technician_detail_count)

        dest?.writeString(pending_parts_verification_status_count)
        dest?.writeTypedList(warranty_parts)
        dest?.writeTypedList(lead_enquiry_images)
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
