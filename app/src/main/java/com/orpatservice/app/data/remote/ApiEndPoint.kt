package com.orpatservice.app.data.remote

import com.google.gson.JsonObject
import com.orpatservice.app.data.model.AddTechnicianResponse
import com.orpatservice.app.data.model.RepairPartResponse
import com.orpatservice.app.data.model.SaveEnquiryResponse
import com.orpatservice.app.data.model.login.OTPSendResponse
import com.orpatservice.app.data.model.TechnicianResponse
import com.orpatservice.app.data.model.login.LoginResponse
import com.orpatservice.app.data.model.requests_leads.CancelLeadResponse
import com.orpatservice.app.data.model.requests_leads.RequestLeadResponse
import com.orpatservice.app.ui.leads.customer_detail.CancelRequestResponse
import com.orpatservice.app.ui.leads.customer_detail.UpdateRequestResponse
import com.orpatservice.app.ui.leads.customer_detail.UploadFileResponse
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.NewRequestResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiEndPoint {

    @FormUrlEncoded
    @POST("service-center/login")
    fun getServiceCenterLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    //To get OTP on entered mobile number
    @FormUrlEncoded
    @POST("service-center/send-otp")
    fun getServiceCenterOtpAPI(@Field("mobile") mobile: String): Call<OTPSendResponse>

    @FormUrlEncoded
    @POST("technician/send-otp")
    fun getTechnicianOtpAPI(@Field("mobile") mobile: String): Call<OTPSendResponse>

    //@POST("technician/send-otp")
   // fun getTechnicianOtpAPI(@Body params: JsonObject): Call<OTPSendResponse>


    //To verify OTP and login
    @FormUrlEncoded
    @POST("service-center/verify-otp-and-login")
    fun verifyServiceCenterOTPLoginAPI(
        @Field("mobile") mobile: String,
        @Field("otp") otp: String
    ): Call<LoginResponse>

    //To verify OTP and login
    @FormUrlEncoded
    @POST("technician/verify-otp-and-login")
    fun verifyTechnicianOTPLoginAPI(
        @Field("mobile") mobile: String,
        @Field("otp") otp: String
    ): Call<LoginResponse>

    @GET("service-center/technicians")
    fun getTechnicianAPI(@Query("page") page: Int?): Call<TechnicianResponse>

    @POST("service-center/leads/{leadsId}/technician/{technicianId}")
    fun hitAPIAssignTechnician(
        @Path("leadsId") leadsId: Int?,
        @Path("technicianId") technicianId: Int?
    ): Call<TechnicianResponse>

    @GET("technician/get_parts")
    fun hitAPIParts(@Query("search") search: String?): Call<RepairPartResponse>

    @POST("technician/send-happy-code/{leadId}")
    fun hitAPISendHappyCode(@Path("leadId") leadId: String?): Call<TechnicianResponse>

    @FormUrlEncoded
    @POST("technician/leads/mark-as-complete/{leadId}")
    fun hitAPIMarkAsComplete(
        @Path("leadId") leadId: String?,
        @Field("remark") remark: String?,
        @Field("happy_code") happy_code: String?
    ): Call<SaveEnquiryResponse>

    // @FormUrlEncoded
    @POST("service-center/technicians")
    fun hitAPIAddTechnician(
        @Body requestBody: MultipartBody
    ): Call<AddTechnicianResponse>

    // @FormUrlEncoded
    @POST("technician/leads/save-enquiry-detail/{complaint_id}/{technician_id}")
    fun hitAPIRepairPartTechnician(
        @Body requestBody: MultipartBody,
        @Path("complaint_id") complaint_id: String,
        @Path("technician_id") technician_id: String
    ): Call<SaveEnquiryResponse>

    @POST("service-center/technicians/update_technician/{technician_id}")
    fun hitAPIUpdateTechnician(
        @Body requestBody: MultipartBody,
        @Path("technician_id") technician_id: Int?
    ): Call<AddTechnicianResponse>

    @POST("service-center/upload-file")
    fun hitAPIUploadFile(
        @Header("Authorization") token : String,
        @Body requestBody: MultipartBody
    ): Call<UploadFileResponse>

    @POST("service-center/leads/{leadsId}/task/{taskId}")
    fun hitUpdateRequestLead(
        @Path("leadsId") leadsId: Int?,
        @Path("taskId") taskId: Int?,
        @Header("Authorization") token : String,
        @Body body : JsonObject

    )
    : Call<UpdateRequestResponse>

    @POST("service-center/leads/cancel_lead/{leadsId}")
    fun hitCancelRequestLead(
        @Header("Authorization") token : String,
        @Body body : JsonObject,
        @Path("leadsId") leadsId: Int?,
    )
    : Call<CancelRequestResponse>


    @GET("service-center/leads/pending")
    fun getServiceCenterPendingLeads(@Header("Authorization") token : String,@Query("page") page: Int): Call<RequestLeadResponse>


    @GET("service-center/leads/assigned")
    fun getAssignTechnicianLeads(@Header("Authorization") token : String): Call<RequestLeadResponse>


    @GET("service-center/leads/pending")
    fun getServiceCenterSearchPendingLeads(@Query("search") keyword: String): Call<RequestLeadResponse>

    @GET("service-center/leads/assigned")
    fun getServiceCenterAssignedLeads(@Query("page") page: Int): Call<RequestLeadResponse>

    @GET("service-center/technicians")
    fun getServiceCenterAssignedTechnicianLeads(@Header("Authorization") token : String,@Query("page") page: Int): Call<NewRequestResponse>


    @GET("service-center/leads/assigned")
    fun getServiceCenterSearchAssignedLeads(@Query("search") keyword: String): Call<RequestLeadResponse>

    @GET("service-center/leads/completed")
    fun getServiceCenterCompletedLeads(@Query("page") page: Int): Call<RequestLeadResponse>

    @GET("service-center/leads/cancelled")
    fun getServiceCenterCancelledLeads(@Query("page") page: Int): Call<RequestLeadResponse>

    @POST("service-center/leads/cancel_lead/{lead_id}")
    fun getServiceCenterCancelLead(
        @Path("lead_id") leadId: Int,
        @Query("lead_cancelled_reason") cancelReason: String
    ): Call<CancelLeadResponse>

    @GET("technician/leads/pending")
    fun getTechnicianPendingLeads(@Query("page") page: Int): Call<RequestLeadResponse>

    @GET("technician/leads/pending")
    fun getTechnicianSearchPendingLeads(@Query("search") keyword: String): Call<RequestLeadResponse>

    @GET("technician/leads/completed")
    fun getTechnicianCompletedLeads(@Query("page") page: Int): Call<RequestLeadResponse>
}