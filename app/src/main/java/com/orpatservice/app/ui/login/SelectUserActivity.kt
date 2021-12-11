package com.orpatservice.app.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.orpatservice.app.R

import android.widget.TextView




class SelectUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_user)

        //supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        //supportActionBar?.setCustomView(R.layout.actionbar_layout)
        //(supportActionBar?.customView?.findViewById(R.id.tvTitle) as TextView).text = getString(R.string.step_1)

    }
}