package com.orpatservice.app.ui.force_update

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.orpatservice.app.databinding.ActivityForceUpdateBinding
import com.orpatservice.app.utils.Constants

class ForceUpdateActivity : AppCompatActivity() {

    /* @Inject
     lateinit var mAnalyticsService: AnalyticsService*/

    private lateinit var binding : ActivityForceUpdateBinding
    //  private val model : ForceUpdateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForceUpdateBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)
        supportActionBar?.hide()

        /* mAnalyticsService.trackEvent(
             AnalyticsEvent(
                 eventName = AnalyticsEventsConstants.APP_UPDATED,
                 action = "",
             )
         )*/
        var play_url = intent.getStringExtra("play_url")
        if(play_url.isNullOrBlank())
            play_url = Constants.PLAYSTORE_URL

        play_url.let{

            binding.updateBtn.setOnClickListener {
                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                intent.addCategory(Intent.CATEGORY_BROWSABLE)
                intent.data = Uri.parse(play_url)
                startActivity(intent)
            }
        }
    }
}