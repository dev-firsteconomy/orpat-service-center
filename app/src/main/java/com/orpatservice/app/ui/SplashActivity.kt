package com.orpatservice.app.ui

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import com.orpatservice.app.R
import com.orpatservice.app.ui.dashboard.DashboardActivity
import com.orpatservice.app.ui.data.sharedprefs.SharedPrefs
import com.orpatservice.app.ui.login.SelectUserActivity
import com.orpatservice.app.ui.technician.TechnicianDashboardActivity
import com.orpatservice.app.utils.Constants
import com.tapadoo.alerter.Alerter

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // This is used to hide the status bar and make
        // the splash screen as a full screen activity.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
        Handler(Looper.getMainLooper()).postDelayed({
            if (SharedPrefs.getInstance().getString(Constants.TOKEN, Constants.NO_TOKEN).isNullOrEmpty()) {
                val intent = Intent(this, SelectUserActivity::class.java)
                startActivity(intent)
            } else {
                /**
                 * User navigation after login
                 */
                if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.ADMIN)) {
                    val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                } else if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.TECHNICIAN)) {
                    val intent = Intent(this, TechnicianDashboardActivity::class.java)
                    startActivity(intent)
                } else {
                    Alerter.create(this)
                        .setText("Something went wrong!!!")
                        .setBackgroundColorRes(R.color.orange)
                        .setDuration(1000)
                        .show()
                }
            }

            finish()
        }, 3000)// 3000 is the delayed time in milliseconds.
    }
}