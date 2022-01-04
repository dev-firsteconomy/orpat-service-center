package com.orpatservice.app.utils

import android.widget.EditText
import java.text.SimpleDateFormat

object CommonUtils {
    fun dateFormat(strDate: String ?): String {
        val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val formatter = SimpleDateFormat("dd MMM yy HH:mm")
        return formatter.format(parser.parse(strDate))
    }
}