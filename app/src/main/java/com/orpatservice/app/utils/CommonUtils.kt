package com.orpatservice.app.utils

import com.orpatservice.app.ui.leads.customer_detail.ProductData
import java.text.SimpleDateFormat

object CommonUtils {

    val productData = ArrayList<ProductData>()

    fun dateFormat(strDate: String ?): String {
        val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val formatter = SimpleDateFormat("dd MMM yy HH:mm")
        return formatter.format(parser.parse(strDate))
    }
}