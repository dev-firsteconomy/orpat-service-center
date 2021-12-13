package com.orpatservice.app.ui.data.repository

import com.orpatservice.app.ui.data.sharedprefs.SharedPrefs

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
            String.format("%s", raw)
        }
    }

    private fun getRawHeaderToken(): String {
        return "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiI2IiwianRpIjoiYTRkYjMzODAzZGE4MzczNGNjMTA2OTIwNzIyM2FhYjM0NmMxODU5ODYwZTRhMGIyYmE1ZmVkZTEwOGVjNzZjMzdiOGY2MDg5YmE0MWY3N2IiLCJpYXQiOjE2MzkxMzczODEuMTU4OTIzLCJuYmYiOjE2MzkxMzczODEuMTU4OTI1LCJleHAiOjE2NzA2NzMzODEuMTU1Mjc2LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.leYs_TdyQoj32kZFN20tQcbwZv2BtwSQfGfTf3p1Y-OEwbSX-AKfKLYoUjtnbImRUqoP3I-rNsbXNaPLpQSSl82PMD84VvxehRTbiamHdyECZ-uYzmacsHMowL9G5bIwDTafZA6SXMxS7DzaX7kGzJVLKTpu62XGDnFT3_b-XK41JXQGhoSeICpOMfnG4XhV7DuvQF73WhjqMTzkeHX5AT48dTkSBVv9wGejoOicNSgqK9F8VgJGVtiDMA3rqU1NORoqClXyj8UQKdXRDJ34hYzNJGQFmrqsh06jptqU-jcyLl_Y7K6cunPl7Q2xcOtu7DBftnFOCdliyKX1oSPRLvFObrOCVS2ciCr0OdwUULGRWYxnJmMrkw0C1utlqqBn1twrTmVOUb3hoz0oQcVyOgymUc6Yu_TLS6r6bn39-eLpbVe-gu7L8v6cFeUrNdPnE5ZpnEpli4qojGiwQmeoLX4m8jUqn4V6V5dWjqe0GbxW4YnYbO2pdnStSu48roRBpTp8T7Jmulm-m3fJJEP-lw97LtSFMiMR8BJtkriUwrryIN7TaUvzv5IC-PaYxP_2_IhzuEaMqnjdhSAcuoRXnvo4csFcB_YcLxFNEahCdTPX_DCICM3jiMZVcKz2KShsR9PONgKbmRVsNSOa5gC8Wv5EYG67_e9n9tGWdBSnL9o"
    }

    //Basic Authentication credentials
    /* private fun getRawHeaderToken(): String {
         return Credentials.basic("admin", "1234")
     }*/

    fun getDeviceId(): String {
        return SharedPrefs.getInstance().getString("DEVICE_ID", "0")
    }
}