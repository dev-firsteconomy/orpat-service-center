package com.orpatservice.app.ui

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.messaging.FirebaseMessaging
import com.moengage.firebase.MoEFireBaseHelper
import com.orpatservice.app.BuildConfig
import com.orpatservice.app.R
import com.orpatservice.app.data.Status
import com.orpatservice.app.data.sharedprefs.SharedPrefs
import com.orpatservice.app.databinding.ActivityAssignedDetailsBinding
import com.orpatservice.app.ui.admin.dashboard.DashboardActivity
import com.orpatservice.app.ui.admin.notification.YourNotificationServiceExtension
import com.orpatservice.app.ui.force_update.ForceUpdateActivity
import com.orpatservice.app.ui.login.SelectUserActivity
import com.orpatservice.app.ui.login.UserLoginViewModel
import com.orpatservice.app.utils.Constants
import com.pushwoosh.Pushwoosh
import com.pushwoosh.RegisterForPushNotificationsResultData
import com.pushwoosh.exception.PushwooshException
import com.pushwoosh.exception.RegisterForPushNotificationsException
import com.pushwoosh.function.Result
import org.json.JSONException
import org.json.JSONObject


class SplashActivity : AppCompatActivity(){
    private lateinit var viewModel : UserLoginViewModel
    private var page_model : String = ""
    private lateinit var cpiLoading: CircularProgressIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        viewModel = ViewModelProvider(this)[UserLoginViewModel::class.java]

        cpiLoading = findViewById<CircularProgressIndicator>(R.id.cpi_loading)

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

        page_model = intent.getStringExtra(Constants.MODULE_TYPE).toString()
        //println("page_model"+"")
/*

        if (intent.extras != null) {
            for (key in intent.extras!!.keySet()) {
                val value = intent.extras!![key]
                println("valuevaluevaluevalue"+value.toString())
                //Log.d("MainActivity: ", "Key: $key Value: $value")
            }
        }
*/

        val bundle = intent.extras
        if (bundle != null) {
            openPage(bundle.get("page_name").toString())
            //bundle must contain all info sent in "data" field of the notification
        }

        updateApi()
        utilUI()


        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.

    }



    private fun utilUI() {

        Pushwoosh.getInstance().registerForPushNotifications()

        Pushwoosh.getInstance()
            .registerForPushNotifications { result: Result<RegisterForPushNotificationsResultData?, RegisterForPushNotificationsException?> ->
                if (result.isSuccess) {
                    val token: String? = result.getData()?.token
                   println("result.getData()?.token"+result.getData()?.token)
                    // handle successful registration


                } else {
                    val exception: PushwooshException? = result.exception
                    // handle registration error
                }
            }

       /* Pushwoosh.getInstance().getTags { result ->
            if (result.isSuccess()) {
                // tags sucessfully received
                val intTag: String? = result.data!!.getString("")
            } else {
                // failed to receive tags
            }
        }*/

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                println("Fetching FCM registration token failed"+ task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            Constants.REGISTRATION_TOKEN = task.result.toString()

            MoEFireBaseHelper.getInstance().passPushToken(applicationContext,token)

            // Log and toast
//            val msg = getString(R.string.msg_token_fmt, token)
//            Log.d(TAG, msg)
//            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })
    }

    private fun updateApi() {

        //if (getIntent().hasExtra(FirebaseMessaging.getInstance().)) {
        if (getIntent().hasExtra(Pushwoosh.PUSH_RECEIVE_EVENT)) {
            // Activity was started in response to push notification
            var data   = getIntent().getExtras()?.getString(Pushwoosh.PUSH_RECEIVE_EVENT)

            try {
                val json = JSONObject(data.toString())
                val userdata = json.getString("userdata")
                val json1 = JSONObject(userdata.toString())
                val page = json1.getString("page")

              //  openPage(page)
                println("pagepagepagepage "+page)

            } catch (e: JSONException) {
                e.printStackTrace()
            }

           // println("Push message is " + getIntent().getExtras()?.getString(Pushwoosh.PUSH_RECEIVE_EVENT));

        }

        val deviceID = Settings.Secure.getString(
            this.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        SharedPrefs.getInstance().addUpdateString(Constants.DEVICE_ID, deviceID)

        val appVersionCode = BuildConfig.VERSION_CODE

        viewModel.checkForceUpdate(deviceID,appVersionCode)


        viewModel.forceUpdateResponse.observe(this@SplashActivity) {

            when(it.status){
                Status.LOADING -> {
                    cpiLoading.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    cpiLoading.visibility = View.VISIBLE
                }
             else -> {
                 cpiLoading.visibility = View.GONE
                 if (it.data?.data!!.is_force_update) {
                     val i = Intent(this@SplashActivity, ForceUpdateActivity::class.java)
                     i.putExtra("play_url", Constants.PLAYSTORE_URL)
                     startActivity(i)
                 } else {
                     Handler(Looper.getMainLooper()).postDelayed({
                         if (SharedPrefs.getInstance().getString(Constants.TOKEN, Constants.NO_TOKEN).isNullOrEmpty()) {
                             val intent = Intent(this, SelectUserActivity::class.java)
                             startActivity(intent)
                         } else {
                             val intent = Intent(this, DashboardActivity::class.java)
                             startActivity(intent)
                         }

                         finish()
                     }, 3000)// 3000 is the delayed time in milliseconds.
                 }
             }
        }
           /* if (it.data?.data!!.is_force_update) {
                val i = Intent(this@SplashActivity, ForceUpdateActivity::class.java)
                i.putExtra("play_url", Constants.PLAYSTORE_URL)
                startActivity(i)
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    if (SharedPrefs.getInstance().getString(Constants.TOKEN, Constants.NO_TOKEN).isNullOrEmpty()) {
                        val intent = Intent(this, SelectUserActivity::class.java)
                        startActivity(intent)
                    } else {
                        val intent = Intent(this, DashboardActivity::class.java)
                        startActivity(intent)
                    }

                    finish()
                }, 3000)// 3000 is the delayed time in milliseconds.
            }*/
        }
    }

    private fun openPage(page: String) {
        println("ssss"+SharedPrefs.getInstance().getString(Constants.USER_TYPE, "")
            .equals(Constants.SERVICE_CENTER))


        if (!SharedPrefs.getInstance().getString(Constants.USER_TYPE, "")
                .equals(Constants.SERVICE_CENTER)) {

            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra(Constants.RECEIVE_DATA, page)
            startActivity(intent)
            finish()

        } else {
            if(page.equals("Technician_New_Request")){


            }else {
                val intent = Intent(this, DashboardActivity::class.java)
                intent.putExtra(Constants.RECEIVE_DATA, page)
                startActivity(intent)
                finish()
            }
        }

           /* val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra(Constants.RECEIVE_DATA, page)
            startActivity(intent)
            finish()*/

    }
}


