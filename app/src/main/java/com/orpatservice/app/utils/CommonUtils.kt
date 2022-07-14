package com.orpatservice.app.utils

import com.orpatservice.app.data.model.requests_leads.ImageData
import com.orpatservice.app.data.model.requests_leads.ImageListData
import com.orpatservice.app.data.model.requests_leads.ImagePosition
import com.orpatservice.app.ui.admin.technician.PinData
import com.orpatservice.app.ui.leads.customer_detail.CheckWarrantyList
import com.orpatservice.app.ui.leads.customer_detail.ProductData
import com.orpatservice.app.ui.leads.customer_detail.productListData
import java.text.SimpleDateFormat

object CommonUtils {

    val productData = ArrayList<ProductData>()

    val productListData = ArrayList<productListData>()


    val imageList = ArrayList<ImageListData>()

    val imageData = ArrayList<ImageData>()


    val imagePos = ArrayList<ImagePosition>()

    val warrantyListData = ArrayList<CheckWarrantyList>()


    val pincodeData = ArrayList<PinData>()


    fun dateFormat(strDate: String ?): String {
        val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val formatter = SimpleDateFormat("dd MMM yy HH:mm")
        return formatter.format(parser.parse(strDate))
    }
}