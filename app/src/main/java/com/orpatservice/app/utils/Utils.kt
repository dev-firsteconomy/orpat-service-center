package com.orpatservice.app.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.orpatservice.app.R
import com.orpatservice.app.databinding.ItemComplaintBinding
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

    fun validatePinCode(editText: EditText?): Boolean {
        return when {
            editText?.text?.isEmpty()!! -> {
                editText.error = editText.context.getString(R.string.pin_code_required)
                editText.requestFocus()
                false
            }
            editText.text?.length != 6 -> {
                editText.error = editText.context.getString(R.string.pin_code_invalid)
                editText.requestFocus()
                return false
            }
            else -> true
        }
    }

    fun validateEmail(editText: EditText?): Boolean {
        return when {
            editText?.text?.isEmpty()!! -> {
                editText.error = editText.context.getString(R.string.error_email)
                editText.requestFocus()
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(editText.text).matches() -> {
                editText.error = editText.context.getString(R.string.error_email)
                editText.requestFocus()
                return false
            }
            else -> true
        }
    }

    fun validateEditText(editText: EditText?): Boolean {
        return when {
            editText?.text?.isEmpty()!! -> {
                editText.error = editText.context.getString(R.string.error_reason_rejection)
                editText.requestFocus()
                false
            }
            else -> true
        }
    }


    fun validatePassword(editText: EditText?): Boolean {
        return when {
            editText?.text?.isEmpty()!! -> {
                editText.error = editText.context.getString(R.string.error_password)
                editText.requestFocus()
                false
            }
            else -> true
        }
    }

    fun validateDate(txt: TextView?): Boolean {
        return if (txt?.text?.isEmpty()!!) {
            txt.error = txt.context.getString(R.string.select_date)
            txt.requestFocus()
            false

        } else true
    }

    fun validateWarranty(binding: ItemComplaintBinding?, view: View): Boolean {

        return if(binding?.rbGroup!!.getCheckedRadioButtonId() == -1){
            Toast.makeText(view.context.getApplicationContext(), "Please select Warranty", Toast.LENGTH_SHORT).show();
            false
        } else true
    }

   /* fun validateImage(img: ImageView?, view: View): Boolean {

       return if(img.is){
           Toast.makeText(view.context.getApplicationContext(), "Please upload invoice", Toast.LENGTH_SHORT).show();
           false
       }else true
    }*/
   fun validateReason(editText: EditText?): Boolean {
       return if (editText?.text?.isEmpty()!!) {
           editText.error = editText.context.getString(R.string.service_center_cancel_reason)
           editText.requestFocus()
           false

       } else true
   }


    fun validateDescription(editText: EditText?): Boolean {
        return if (editText?.text?.isEmpty()!!) {
            editText.error = editText.context.getString(R.string.service_center_discription)
            editText.requestFocus()
            false

        } else true
    }

    fun validateInvoice(editText: EditText?): Boolean {
        return if (editText?.text?.isEmpty()!!) {
            editText.error = editText.context.getString(R.string.invoicenumber)
            editText.requestFocus()
            false

        } else true
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

    public fun popupUtil(context: Context, title : String, desc :String?, isSuccessPopup: Boolean) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.popup_layout, null)
        val popupIcon = view.findViewById<ImageView>(R.id.popup_icon)
        var popupTitle = view.findViewById<TextView>(R.id.popup_tv_heading)
        val popupDesc = view.findViewById<TextView>(R.id.popup_tv_desc)
        val alertDialogBuilder = Dialog(context)
        alertDialogBuilder.setContentView(view)
        alertDialogBuilder.show()

        popupTitle.text = title

        desc?.let {
            popupDesc.text = it
        }

        if(isSuccessPopup){

            popupTitle.setTextColor(ContextCompat.getColor(context, R.color.success_green))
           /* Glide.with(context)
                .load(R.drawable.ic_verified_icon)
                .into(popupIcon)*/

        }else{
            popupTitle.setTextColor(ContextCompat.getColor(context, R.color.failure_red))
           /* Glide.with(context)
                .load(R.drawable.ic_error_icon)
                .into(popupIcon)*/


        }

    }
}
