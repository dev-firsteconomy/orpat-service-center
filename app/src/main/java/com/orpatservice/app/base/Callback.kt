package com.orpatservice.app.base

import android.view.View

interface Callback {
    fun onItemClick(view : View, position : Int)
}