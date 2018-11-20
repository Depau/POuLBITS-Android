package org.poul.bits.android.model

import android.os.Parcel
import org.poul.bits.android.commons.KParcelable
import org.poul.bits.android.commons.parcelableCreator
import java.util.*

data class BitsMessage(
    val user: String,
    val message: String,
    val lastModified: Date
) : KParcelable {
    constructor(parcel: Parcel) : this(
        user = parcel.readString()!!,
        message = parcel.readString()!!,
        lastModified = parcel.readSerializable() as Date
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(user)
        parcel.writeString(message)
        parcel.writeSerializable(lastModified)
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::BitsMessage)
    }
}