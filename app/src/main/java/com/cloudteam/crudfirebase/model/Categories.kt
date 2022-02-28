package com.cloudteam.crudfirebase.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Categories(
    var strId: String? = "0",
    var nameCategory: String? = null
) : Parcelable