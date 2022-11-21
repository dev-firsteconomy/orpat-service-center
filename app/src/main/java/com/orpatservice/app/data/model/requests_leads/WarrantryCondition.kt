package com.orpatservice.app.data.model.requests_leads

import android.os.Parcel
import android.os.Parcelable

class WarrantryCondition() : Parcelable {
    var id: Int? = null
    var title: String? = null


    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        title = parcel.readString()

    }


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeValue(id)
        dest?.writeString(title)
    }

    companion object CREATOR : Parcelable.Creator<WarrantryCondition> {
        override fun createFromParcel(parcel: Parcel): WarrantryCondition {
            return WarrantryCondition(parcel)
        }

        override fun newArray(size: Int): Array<WarrantryCondition?> {
            return arrayOfNulls(size)
        }
    }
}
