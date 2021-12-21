package com.orpatservice.app.utils

import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import android.widget.EditText
import com.orpatservice.app.R
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.system.exitProcess

class Utils {
    companion object {
        val instance = Utils()
    }
    fun validateFirstName(editText: EditText?): Boolean {
        return if (editText?.text?.isEmpty()!!) {
            editText.error = editText.context.getString(R.string.first_name_required)
            editText.requestFocus()
            false

        } else true

    }

    fun validateLastName(editText: EditText?): Boolean {
        return if (editText?.text?.isEmpty()!!) {
            editText.error = editText.context.getString(R.string.last_name_required)
            editText.requestFocus()
            false

        } else true

    }
    fun validatePhoneNumber(editText: EditText?): Boolean {
        return when {
            editText?.text?.isEmpty()!! -> {
                editText.error = editText.context.getString(R.string.phone_number_required)
                editText.requestFocus()
                false

            }
            editText.text?.length != 10 -> {
                editText.error = editText.context.getString(R.string.number_invalid)
                editText.requestFocus()
                return false

            }
            else -> true
        }

    }

    fun reSizeImg(bm: Bitmap) : Uri {
        var imageFile : File?=null
        var bm: Bitmap? = bm
        try {
            if (bm == null) {
                exitProcess(1)
            }
            var maxHeight = 700
            var maxWidth = 700
            if (bm.width < 700) {
                maxHeight = bm.height
                maxWidth = bm.width
            }
            val scale = Math.min(
                maxHeight.toFloat() / bm.width,
                maxWidth.toFloat() / bm.height
            )
            val scaleThumb = Math.min(64.toFloat() / bm.width, 64.toFloat() / bm.height)
            val matrix = Matrix()
            matrix.postScale(scale, scale)
            val resizedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.width, bm.height, matrix, true)
            matrix.postScale(scaleThumb, scaleThumb)
            val thumbBitmap = Bitmap.createBitmap(bm, 0, 0, bm.width, bm.height, matrix, true)
            bm.recycle()
            bm = null
            val files = Environment.getExternalStorageDirectory()
            val rootFolder = File(files, "orpatservicecenter")
            if (!rootFolder.exists()) rootFolder.mkdir()
            val subFolder = File(rootFolder, "images")
            if (!subFolder.exists()) subFolder.mkdir()
            val name = UUID.randomUUID().toString()
            imageFile = File(subFolder, "$name.jpg")
            val fos = FileOutputStream(imageFile)
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos)
            fos.flush()
            fos.close()

            //"file:///"+ imageFile.getPath()
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
        return Uri.fromFile(imageFile)
    }
}
