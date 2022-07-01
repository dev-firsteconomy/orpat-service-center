package com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request

import android.os.Parcel
import android.os.Parcelable
import com.orpatservice.app.data.model.requests_leads.Enquiry
import com.orpatservice.app.data.model.requests_leads.WarrantryPart
import com.orpatservice.app.ui.leads.technician.response.TechnicianEnquiryImage
import com.orpatservice.app.utils.Constants

class TaskEnquiry() : Parcelable {
    var id: Int? = null
    //var id: String? = null
    var invoice_url: String? = null
    var scanned_barcode: String? = null
    var purchase_at: String? = null
    var nature_pf_complain: String? = null
    var in_warranty: String? = null
    //var status: Boolean = false
    var status: String? = null
    var customer_discription: String? = null
    var dummy_barcode: String? = null
    var model_no: String? = null
    var pending_parts_verification_status_count: String? = null
    var detail_status: Int? = null
    var warranty_parts: ArrayList<WarrantryPart> = arrayListOf()
    var lead_enquiry_images: ArrayList<TechnicianEnquiryImage> = arrayListOf()



    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
       // id = parcel.readString()
        invoice_url = parcel.readString()
        scanned_barcode = parcel.readString()
        purchase_at = parcel.readString()
        nature_pf_complain = parcel.readString()
        in_warranty = parcel.readString()
        status = parcel.readString()
       // status = parcel.readValue(Boolean::class.java.classLoader) as Boolean
        customer_discription = parcel.readString()
        dummy_barcode = parcel.readString()
        model_no = parcel.readString()
        pending_parts_verification_status_count = parcel.readString()
        detail_status = parcel.readValue(Int::class.java.classLoader) as? Int
        parcel.readTypedList(warranty_parts,WarrantryPart.CREATOR)
        parcel.readTypedList(lead_enquiry_images, TechnicianEnquiryImage.CREATOR)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeValue(id)
        //dest?.writeString(id)
        dest?.writeString(invoice_url)
        dest?.writeString(scanned_barcode)
        dest?.writeString(purchase_at)
        dest?.writeString(nature_pf_complain)
        dest?.writeString(in_warranty)
       // dest?.writeValue(status)
        dest?.writeString(status)
        dest?.writeString(customer_discription)
        dest?.writeString(dummy_barcode)
        dest?.writeString(model_no)
        dest?.writeString(pending_parts_verification_status_count)
        dest?.writeValue(detail_status)
        dest?.writeTypedList(warranty_parts)
        dest?.writeTypedList(lead_enquiry_images)
    }

    companion object CREATOR : Parcelable.Creator<TaskEnquiry> {
        override fun createFromParcel(parcel: Parcel): TaskEnquiry {
            return TaskEnquiry(parcel)
        }

        override fun newArray(size: Int): Array<TaskEnquiry?> {
            return arrayOfNulls(size)
        }
    }
}
