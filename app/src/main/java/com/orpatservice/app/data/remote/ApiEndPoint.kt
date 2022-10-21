package com.orpatservice.app.data.remote

import com.google.gson.JsonObject
import com.orpatservice.app.data.model.AddTechnicianResponse
import com.orpatservice.app.data.model.RepairPartResponse
import com.orpatservice.app.data.model.SaveEnquiryResponse
import com.orpatservice.app.data.model.login.OTPSendResponse
import com.orpatservice.app.data.model.TechnicianResponse
import com.orpatservice.app.data.model.login.LoginResponse
import com.orpatservice.app.data.model.login.SendHappyCodeResponse
import com.orpatservice.app.data.model.login.TechnicianLoginResponse
import com.orpatservice.app.data.model.requests_leads.CancelLeadResponse
import com.orpatservice.app.data.model.requests_leads.RequestLeadResponse
import com.orpatservice.app.ui.admin.dashboard.RequestSynAppResponse
import com.orpatservice.app.ui.admin.dashboard.RequestTechnicianSynAppResponse
import com.orpatservice.app.ui.admin.technician.RequestPincodeResponse
import com.orpatservice.app.ui.admin.technician.RequestTechnicianData
import com.orpatservice.app.ui.leads.customer_detail.CancelRequestResponse
import com.orpatservice.app.ui.leads.customer_detail.UpdateRequestResponse
import com.orpatservice.app.ui.leads.customer_detail.UploadFileResponse
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.NewRequestResponse
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.TakCompletedResponse
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.UpdatePartsRequestData
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.VerifyGSTRequestData
import com.orpatservice.app.ui.leads.technician.TechnicianUpdateRequestResponse
import com.orpatservice.app.ui.leads.technician.ValidateProductResponse
import com.orpatservice.app.ui.leads.technician.response.TechnicianRequestLeadResponse
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

    @FormUrlEncoded
    @POST("technician/verify-otp-and-login")
    fun verifyTaskOTPAPI(
        @Field("mobile") mobile: String,
        @Field("otp") otp: String
    ): Call<LoginResponse>


    @GET("service-center/technicians")
    fun getTechnicianAPI( @Header("Authorization") token : String,@Query("page") page: Int?): Call<TechnicianResponse>

    @GET("service-center/technicians")
    fun getTechnicianDataAPI( @Header("Authorization") token : String,@Query("page") page: Int?): Call<RequestTechnicianData>


    @POST("service-center/leads/{leadsId}/technician/{technicianId}")
    fun hitAPIAssignTechnician(
        @Path("leadsId") leadsId: Int?,
        @Path("technicianId") technicianId: Int?
    ): Call<TechnicianResponse>


   @POST("technician/validate-task-product/{taskId}")
   fun validateProduct(
       @Header("Authorization") token : String,
       @Path("taskId") taskId: String?,
       @Body body : JsonObject
   ): Call<UpdateRequestResponse>

    @FormUrlEncoded
    @POST("customer/validate-product")
    fun validateProduct(
        @Header("Authorization") token : String,
        @Field("scanned_barcode") scanned_barcode: String,
    ): Call<ValidateProductResponse>


    @POST("service-center/leads/{leadsId}/technician/{technicianId}")
    fun hitAPIAssignTechnicianLead(
        @Header("Authorization") token : String,
        @Path("leadsId") leadsId: String?,
        @Path("technicianId") technicianId: String?
    ): Call<TechnicianResponse>

    @POST("service-center/leads/{leadsId}/re-assign-to-technician/{technicianId}")
    fun hitAPIAssignChangeTechnicianLead(
        @Header("Authorization") token : String,
        @Path("leadsId") leadsId: String?,
        @Path("technicianId") technicianId: String?
    ): Call<TechnicianResponse>


    @GET("technician/get_parts")
    fun hitAPIParts(@Query("search") search: String?): Call<RepairPartResponse>

    @POST("technician/send-happy-code/{leadId}")
    fun hitAPISendHappyCode(@Path("leadId") leadId: String?): Call<TechnicianResponse>

    @POST("service-center/send-happy-code/{leadId}")
    fun hitAPITaskSendHappyCode(@Path("leadId") leadId: String?,  @Header("Authorization") token : String): Call<SendHappyCodeResponse>

    @FormUrlEncoded
    @POST("service-center/leads/lead-happy-code-verification/{leadId}")
    fun hitAPITaskSendHappyCodeVerification(
        @Path("leadId") leadId: String?,
        @Header("Authorization") token : String,
        @Field("happy_code") happy_code: String?): Call<UpdatePartsRequestData>

    @FormUrlEncoded
    @POST("technician/leads/mark-as-complete/{leadId}")
    fun hitAPIMarkAsComplete(
        @Path("leadId") leadId: String?,
        @Field("remark") remark: String?,
        @Field("happy_code") happy_code: String?
    ): Call<SaveEnquiryResponse>

    @FormUrlEncoded
    @POST("service-center/leads/mark-as-complete-lead/{leadId}")
    fun hitAPIServiceMarkAsComplete(
        @Header("Authorization") token : String,
        @Path("leadId") leadId: String?,
        @Field("happy_code") happy_code: String?,

    ): Call<UpdatePartsRequestData>

    // @FormUrlEncoded
    @POST("service-center/technicians")
    fun hitAPIAddTechnician(
        @Header("Authorization") token : String,
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
        @Header("Authorization") token : String,
        @Body requestBody: MultipartBody,
        @Path("technician_id") technician_id: Int?
    ): Call<AddTechnicianResponse>

    @POST("service-center/technicians")
    fun submitTechnician(
        @Header("Authorization") token : String,
        @Body body : JsonObject
    ): Call<AddTechnicianResponse>


    @POST("service-center/technicians/update_technician/{technician_id}")
    fun updateTechnician(
        @Header("Authorization") token : String,
        @Body body : JsonObject,
        @Path("technician_id") technician_id: Int?
    ): Call<AddTechnicianResponse>


    @POST("service-center/upload-file")
    fun hitAPIUploadFile(
        @Header("Authorization") token : String,
        @Body requestBody: MultipartBody
    ): Call<UploadFileResponse>

    @POST("technician/upload-file")
    fun hitAPITechnicianUploadFile(
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


  /*  @POST("service-center/enquiry-parts-verification/{leadsId}")
    fun hitTaskUpdateRequestLead(
        @Path("leadsId") leadsId: Int?,
        @Header("Authorization") token : String,
        @Body body : JsonObject

    )
            : Call<UpdateRequestResponse>*/

    @POST("service-center/enquiry-parts-verification/{leadsId}")
    fun hitTaskUpdateRequestLead(
        @Header("Authorization") token : String,
        @Body body : JsonObject,
        @Path("leadsId") leadsId: Int?
    ): Call<UpdateRequestResponse>



    @POST("technician/leads/mark-as-complete/{leadsId}")
    fun hitTaskCompletedRequestLead(
        @Path("leadsId") leadsId: Int?,
        @Header("Authorization") token : String,

    ): Call<TechnicianRequestLeadResponse>


    @POST("technician/update-task/{taskId}")
    fun hitTechnicianUpdateRequestLead(
        @Header("Authorization") token : String,
        @Path("taskId") taskId: Int?,
        @Body body : JsonObject

    ): Call<TechnicianUpdateRequestResponse>


    @POST("service-center/leads/{leadsId}/technician/{technicianId}")
    fun hitTechnicianAssignRequestLead(
        @Path("leadsId") leadsId: Int?,
        @Path("technicianId") taskId: Int?,
        @Header("Authorization") token : String,
    )
    : Call<UpdateRequestResponse>


    @POST("service-center/leads/cancel_lead/{leadsId}")
    fun hitCancelRequestLead(
        @Header("Authorization") token : String,
        @Body body : JsonObject,
        @Path("leadsId") leadsId: Int?,
    )
    : Call<CancelRequestResponse>


    @POST("service-center/leads/chargeable/mark/{leadsId}")
    fun hitChargeableCancelRequestLead(
        @Header("Authorization") token : String,
        @Body body : JsonObject,
        @Path("leadsId") leadsId: Int?,
    )
    : Call<CancelRequestResponse>

    @POST("service-center/leads/cancel_enquiry")
    fun hitTaskCancelRequestLead(
        @Header("Authorization") token : String,
        @Body body : JsonObject,
    ): Call<CancelRequestResponse>

    @POST("technician/leads/cancel_enquiry")
    fun hitTechnicianTaskCancelRequestLead(
        @Header("Authorization") token : String,
        @Body body : JsonObject,
    ): Call<CancelRequestResponse>


    @GET("service-center/app-config")
    fun getSynAppConfig(@Header("Authorization") token : String): Call<RequestSynAppResponse>


    @GET("technician/app-config")
    fun getTechnicianSynAppConfig(@Header("Authorization") token : String): Call<RequestTechnicianSynAppResponse>

    @GET("service-center/leads/pending")
    fun getServiceCenterPendingLeads(@Header("Authorization") token : String,@Query("page") page: Int): Call<RequestLeadResponse>

    @GET("service-center/pincodes")
    fun getPincode(@Header("Authorization") token : String): Call<RequestPincodeResponse>



    @GET("service-center/leads/chargeable")
    fun getServiceCenterChargeableLeads(

        @Header("Authorization") token : String,
        @Query("page") page: Int,
        @Query("filter") filter: Int,
        ): Call<RequestLeadResponse>


    @GET("service-center/leads/assigned")
    fun getAssignTechnicianLeads(@Header("Authorization") token : String): Call<RequestLeadResponse>


    @GET("service-center/leads/pending")
    fun getServiceCenterSearchPendingLeads(@Query("search") keyword: String, @Header("Authorization") token : String): Call<RequestLeadResponse>

    @GET("service-center/verify-gstin-number/{gst_no}")
    fun getServiceCenterVerifyNum(@Path("gst_no") keyword: String, @Header("Authorization") token : String): Call<VerifyGSTRequestData>


    @GET("service-center/leads/assigned")
    fun getServiceCenterAssignedLeads(@Header("Authorization") token : String,@Query("page") page: Int): Call<RequestLeadResponse>

    @GET("service-center/technicians")
    fun getServiceCenterAssignedTechnicianLeads(@Header("Authorization") token : String,@Query("page") page: Int): Call<NewRequestResponse>

    @GET("service-center/asign-technician-list")
    fun getServiceCenterTechnicianLeads(@Header("Authorization") token : String,
                                        @Query("lead_id") leadsId: Int?,
                                        @Query("page") page: Int
    ): Call<NewRequestResponse>


    @GET("service-center/leads/assigned")
    fun getServiceCenterSearchAssignedLeads(@Query("search") keyword: String, @Header("Authorization") token : String): Call<RequestLeadResponse>

    @GET("service-center/leads/completed")
    fun getServiceCenterSearchCompletedLeads(@Query("search") keyword: String, @Header("Authorization") token : String): Call<TakCompletedResponse>

    @GET("service-center/leads/completed")
    fun getAPISearchCompletedLeads(@Query("search") keyword: String, @Header("Authorization") token : String): Call<RequestLeadResponse>


    @GET("service-center/leads/completed")
    fun getServiceCenterCompletedDataLeads(@Query("page") page: Int, @Header("Authorization") token : String): Call<RequestLeadResponse>

    @GET("service-center/leads/complaint-history")
    fun getServiceCenterCompletedLeads(
        @Query("page") page: Int,
        @Query("type") type: String,
        @Header("Authorization") token : String):
            Call<RequestLeadResponse>


    @GET("service-center/leads/complaint-history")
    fun getServiceCenterCancelledLeads(
        @Query("page") page: Int,
        @Query("type") type: String,
        @Header("Authorization") token : String)
    : Call<RequestLeadResponse>

    @POST("service-center/leads/cancel_lead/{lead_id}")
    fun getServiceCenterCancelLead(
        @Path("lead_id") leadId: Int,
        @Query("lead_cancelled_reason") cancelReason: String
    ): Call<CancelLeadResponse>

    @GET("technician/leads/pending")
    fun getTechnicianPendingLeads(@Header("Authorization") token : String,@Query("page") page: Int): Call<TechnicianRequestLeadResponse>

    @GET("technician/leads/pending")
    fun getTechnicianSearchPendingLeads(@Query("search") keyword: String): Call<RequestLeadResponse>

    @GET("technician/leads/completed")
    fun getTechnicianCompletedLeads(@Header("Authorization") token : String,@Query("page") page: Int): Call<RequestLeadResponse>

    @GET("service-center/leads/assigned")
    fun getServiceCenterTaskCompletedLeads(@Header("Authorization") token : String,@Query("page") page: Int): Call<TakCompletedResponse>

}