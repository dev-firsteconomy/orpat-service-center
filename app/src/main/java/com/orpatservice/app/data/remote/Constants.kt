package com.orpatservice.app.data.remote

object Constants {
    const val APP_PREFS_ESTATE_CIRCLE = "ESTATE_CIRCLE_APP_PREFS"

    const val AUTH_TOKEN_KEY = "auth_token"
    const val USER_DATA_KEY = "user_data"
    const val NEW_USER_KEY = "new_user"
    const val PLATFORM_KEY = "platform"
    const val ANDROID_KEY = "android"
    const val MOBILE_KEY = "mobile"

    const val PERMISION_REQUEST = 100
    const val PERIODIC_INTERVAL = 15L
    const val URLFILE = "https://www.maharashtra.gov.in/Site/Upload/Government%20Resolutions/Marathi/202202011751498101.pdf"

    const val APP_FLYER_DEV_KEY = "gSMvZTUoPsV3TheSfsPyP6"

    const val SCANNED = 1
    const val NOTSCANNED = 0

    enum class ScannedStatus{
        SCANNED,
        NOTSCANNED
    }
}