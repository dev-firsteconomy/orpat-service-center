package com.orpatservice.app.ui.data.remote

import com.orpatservice.app.ui.data.model.login.OTPSendResponse
import com.orpatservice.app.ui.data.model.TechnicianResponse
import com.orpatservice.app.ui.data.model.login.LoginResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiEndPoint {

    //To get OTP on entered mobile number
    @FormUrlEncoded
    @POST("send-otp")
    fun getOtpAPI(@Field ("mobile") mobile: String): Call<OTPSendResponse>

    //To verify OTP and login
    @FormUrlEncoded
    @POST("verify-otp-and-login")
    fun verifyOTPandLoginAPI(
        @Field ("mobile") mobile: String,
        @Field ("otp") otp: String): Call<LoginResponse>

    @GET("technicians")
    fun getTechnicianAPI(): Call<TechnicianResponse>

}