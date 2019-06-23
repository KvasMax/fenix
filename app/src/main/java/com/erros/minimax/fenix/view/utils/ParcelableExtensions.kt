package com.erros.minimax.fenix.view.utils

import android.os.Parcel
import android.os.Parcelable

fun Parcelable.toByteArray(): ByteArray {
    val parcel = Parcel.obtain()
    this.writeToParcel(parcel, 0)
    val bytes = parcel.marshall()
    parcel.recycle()
    return bytes
}

fun <P : Parcelable> ByteArray.toParcelable(creator: Parcelable.Creator<P>): P? {
    val parcel = Parcel.obtain()
    parcel.unmarshall(this, 0, this.size)
    parcel.setDataPosition(0)
    val result = creator.createFromParcel(parcel)
    parcel.recycle()
    return result
}