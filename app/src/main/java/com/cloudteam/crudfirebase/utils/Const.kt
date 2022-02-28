package com.cloudteam.crudfirebase.utils


object Const {
    val PATH_COLLECTION = "products"
    val PATH_PRICE = "doublePrice"
    val PATH_COLLECTION_CAT = "category"
    val PATH_COLLECTION_NAME = "nameCategory"

    fun setTimeStamp(): Long {
        val time = (-1 * System.currentTimeMillis())
        return time
    }
}