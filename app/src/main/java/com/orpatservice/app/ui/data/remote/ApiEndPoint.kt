package com.orpatservice.app.ui.data.remote

import com.orpatservice.app.ui.data.model.otp.OTPSendResponse
import com.orpatservice.app.ui.data.model.TechnicianResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiEndPoint {

    //To get OTP on entered mobile number
    @POST("send-otp")
    fun getOtpAPI(@Field ("mobile") mobile: String): Call<OTPSendResponse>

    @GET("technicians")
    fun getTechnicianAPI(): Call<TechnicianResponse>

/*    @FormUrlEncoded
    @POST("mobile-login")
    fun getLogin(
        @Field("email") username: String?,
        @Field("password") password: String?
    ): Call<LoginSignUpOtpResponse?>?*/

}