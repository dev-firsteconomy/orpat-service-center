package com.orpatservice.app.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.orpatservice.app.R
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.activity_select_user.*

class SelectUserActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_user)

        // set toolbar as support action bar
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            title = ""
            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        btn_continue_user.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.btn_continue_user) {
            selectUser()
        }
    }

    private fun selectUser() {
        if(rb_admin.isChecked) {
            goToLogin("ADMIN")
        } else if (rb_technician.isChecked) {
            goToLogin("TECHNICIAN")
        } else {
            Alerter.create(this)
                .setTitle("")
                .setText(getString(R.string.warning_select_admin_technician))
                .setBackgroundColorRes(R.color.orange)
                .setDuration(1000)
                .show()
        }
    }

    private fun goToLogin(userType: String) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}