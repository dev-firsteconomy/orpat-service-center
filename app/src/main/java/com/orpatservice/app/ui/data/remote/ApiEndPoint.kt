package com.orpatservice.app.ui.data.remote

import com.orpatservice.app.ui.data.model.AddTechnicianResponse
import com.orpatservice.app.ui.data.model.login.OTPSendResponse
import com.orpatservice.app.ui.data.model.TechnicianResponse
import com.orpatservice.app.ui.data.model.login.LoginResponse
import com.orpatservice.app.ui.data.model.requests_leads.CancelLeadResponse
import com.orpatservice.app.ui.data.model.requests_leads.RequestLeadResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiEndPoint {

    //To get OTP on entered mobile number
    @FormUrlEncoded
    @POST("service-center/send-otp")
    fun getServiceCenterOtpAPI(@Field ("mobile") mobile: String): Call<OTPSendResponse>

    @FormUrlEncoded
    @POST("technician/send-otp")
    fun getTechnicianOtpAPI(@Field ("mobile") mobile: String): Call<OTPSendResponse>

    //To verify OTP and login
    @FormUrlEncoded
    @POST("service-center/verify-otp-and-login")
    fun verifyServiceCenterOTPLoginAPI(
        @Field ("mobile") mobile: String,
        @Field ("otp") otp: String): Call<LoginResponse>

    //To verify OTP and login
    @FormUrlEncoded
    @POST("technician/verify-otp-and-login")
    fun verifyTechnicianOTPLoginAPI(
        @Field ("mobile") mobile: String,
        @Field ("otp") otp: String): Call<LoginResponse>

    @GET("service-center/technicians")
    fun getTechnicianAPI(): Call<TechnicianResponse>

    @GET("service-center/technicians")
    fun getNextPageTechnicianAPI(@Query("page") page : Int?): Call<TechnicianResponse>

    // @FormUrlEncoded
    @POST("service-center/technicians")
    fun hitAPIAddTechnician(
        @Body requestBody: MultipartBody
    ): Call<AddTechnicianResponse>

    @POST("service-center/technicians/update_technician/{technician_id}")
    fun hitAPIUpdateTechnician(
        @Body requestBody: MultipartBody,
        @Path("technician_id") technician_id : Int?
    ): Call<AddTechnicianResponse>

    @GET("service-center/leads/pending")
    fun getServiceCenterPendingLeads(@Query("page") page : Int): Call<RequestLeadResponse>

    @GET("service-center/leads/pending")
    fun getServiceCenterSearchPendingLeads(@Query ("search") keyword : String): Call<RequestLeadResponse>

    @GET("service-center/leads/assigned")
    fun getServiceCenterAssignedLeads(@Query("page") page : Int): Call<RequestLeadResponse>

    @GET("service-center/leads/assigned")
    fun getServiceCenterSearchAssignedLeads(@Query ("search") keyword: String): Call<RequestLeadResponse>

    @GET("service-center/leads/cancelled")
    fun getServiceCenterCancelledLeads(@Query("page") page : Int): Call<RequestLeadResponse>

    @POST("service-center/leads/cancel_lead/{lead_id}")
    fun getServiceCenterCancelLead(@Path("lead_id") leadId : Int): Call<CancelLeadResponse>

    @GET("technician/leads/pending")
    fun getTechnicianPendingLeads(@Query("page") page : Int): Call<RequestLeadResponse>

    @GET("technician/leads/completed")
    fun getTechnicianCompletedLeads(@Query("page") page : Int): Call<RequestLeadResponse>
}