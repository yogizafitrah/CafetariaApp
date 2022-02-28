package com.cloudteam.crudfirebase.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Products(
    var strId: String? = "0",
    var strName: String? = null,
    var strCategory: String? = null,
    var doublePrice: Float? = 0F,
    var imageUri:String? = null
) : Parcelable


