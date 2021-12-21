package com.orpatservice.app.ui.data.remote

import com.orpatservice.app.ui.data.model.login.OTPSendResponse
import com.orpatservice.app.ui.data.model.TechnicianResponse
import com.orpatservice.app.ui.data.model.login.LoginResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.Path

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

    // @FormUrlEncoded
    @POST("technicians")
    fun hitAPIAddTechnician(
        @Body requestBody: MultipartBody
    ): Call<TechnicianResponse>

    @POST("technicians/update_technician/{technician_id}")
    fun hitAPIUpdateTechnician(
        @Body requestBody: MultipartBody,
        @Path("technician_id") technician_id : Int?
    ): Call<TechnicianResponse>
}