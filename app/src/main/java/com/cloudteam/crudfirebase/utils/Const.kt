package com.cloudteam.crudfirebase.utils

/**
 * Created by rivaldy on 12/25/2019.
 */

object Const {
    val PATH_COLLECTION = "products"
    val PATH_PRICE = "doublePrice"

    fun setTimeStamp(): Long {
        val time = (-1 * System.currentTimeMillis())
        return time
    }
}