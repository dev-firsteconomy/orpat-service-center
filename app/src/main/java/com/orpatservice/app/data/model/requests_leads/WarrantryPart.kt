package com.orpatservice.app.data.model.requests_leads

import android.os.Parcel
import android.os.Parcelable
import com.orpatservice.app.utils.Constants

class WarrantryPart() : Parcelable {
    var id: Int? = null
    var name: String? = null
    //var in_warranty: String? = null
   // var warranty_period: String? = null
    var type:Int = Constants.PARENT
    var warranty_conditions: ArrayList<WarrantryCondition> = arrayListOf()
    var isExpanded: Boolean = false

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        name = parcel.readString()
       // in_warranty = parcel.readString()
       // warranty_period = parcel.readString()
        parcel.readTypedList(warranty_conditions,WarrantryCondition.CREATOR)
    }


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(id)
        dest.writeString(name)
       // dest?.writeString(in_warranty)
        //dest?.writeString(warranty_period)
       // dest?.writeString(in_warranty)
        dest.writeTypedList(warranty_conditions)
    }

    companion object CREATOR : Parcelable.Creator<WarrantryPart> {
        override fun createFromParcel(parcel: Parcel): WarrantryPart {
            return WarrantryPart(parcel)
        }

        override fun newArray(size: Int): Array<WarrantryPart?> {
            return arrayOfNulls(size)
        }
    }
}
