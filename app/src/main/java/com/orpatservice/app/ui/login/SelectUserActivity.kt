package com.orpatservice.app.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.orpatservice.app.R
import kotlinx.android.synthetic.main.activity_select_user.*

class SelectUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_user)

        // set toolbar as support action bar
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

    }
}