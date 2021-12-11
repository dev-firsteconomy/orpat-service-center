package com.orpatservice.app.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.orpatservice.app.R
import com.orpatservice.app.ui.dashboard.DashboardActivity

class SelectUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_user)

        findViewById<TextView>(R.id.tv_heading).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    DashboardActivity::class.java
                )
            )
        }

    }


}