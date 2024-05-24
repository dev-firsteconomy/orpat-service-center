package com.orpatservice.app.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.orpatservice.app.R

class GenericTextWatcher internal constructor(private val currentView: View, private val nextView: View?) :
    TextWatcher {
    override fun afterTextChanged(editable: Editable) { // TODO Auto-generated method stub
        val text = editable.toString()
        when (currentView.id) {
            R.id.edt_digit1 -> if (text.length == 1) nextView!!.requestFocus()
            R.id.edt_digit2 -> if (text.length == 1) nextView!!.requestFocus()
            R.id.edt_digit3 -> if (text.length == 1) nextView!!.requestFocus()
            //You can use EditText4 same as above to hide the keyboard
        }
    }

    override fun beforeTextChanged(
        arg0: CharSequence,
        arg1: Int,
        arg2: Int,
        arg3: Int
    ) { // TODO Auto-generated method stub
    }

    override fun onTextChanged(
        arg0: CharSequence,
        arg1: Int,
        arg2: Int,
        arg3: Int
    ) { // TODO Auto-generated method stub
    }

}