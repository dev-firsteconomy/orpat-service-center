package com.orpatservice.app.data.repository

import com.orpatservice.app.data.sharedprefs.SharedPrefs
import com.orpatservice.app.utils.Constants

/**
 * Created by Vikas Singh on 12/Dec/2021
 */

class SessionRepository {
    companion object {
        val instance = SessionRepository()
    }

    fun getHeaderToken(): String {
        val raw = getRawHeaderToken()
        return if (raw.equals("na", ignoreCase = true)) {
            "na"
        } else {
            String.format("%s", "Bearer $raw")
        }
    }

    private fun getRawHeaderToken(): String {
        return SharedPrefs.getInstance().getString(Constants.TOKEN, Constants.NO_TOKEN)
    }

    //Basic Authentication credentials
    /* private fun getRawHeaderToken(): String {
         return Credentials.basic("admin", "1234")
     }*/

    fun getDeviceId(): String {
        return SharedPrefs.getInstance().getString("DEVICE_ID", "0")
    }
}