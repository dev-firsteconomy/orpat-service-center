package com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.orpatservice.app.data.Resource
import com.orpatservice.app.data.remote.ErrorUtils
import com.orpatservice.app.data.repository.DataRepository
import com.orpatservice.app.ui.leads.customer_detail.UploadFileResponse
import com.orpatservice.app.ui.leads.technician.ValidateProductResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductViewModel : ViewModel() {

    val qrCodeData = MutableLiveData<Resource<ValidateProductResponse>>()
    val invoiceUploadData = MutableLiveData<Resource<UploadFileResponse>>()
    val submitLeadData = MutableLiveData<Resource<ValidateProductResponse>>()
   // val notValidQRcode = MutableLiveData<Resource<ValidateQrcodeResponse>>()


    fun hitValidateQRApi(qrcode : String){
        DataRepository.instance.hitServiceValidateProductApi(qrcode).enqueue(callbackQRCodeCheck)
    }

    private val callbackQRCodeCheck: Callback<ValidateProductResponse> = object :
        Callback<ValidateProductResponse> {
        override fun onResponse(
            call: Call<ValidateProductResponse>,
            response: Response<ValidateProductResponse>
        ) {
            if (response.isSuccessful) {
                qrCodeData.value = response.body()?.let { Resource.success(it) }
            } else {
                qrCodeData.value =
                    Resource.error(
                        ErrorUtils.getError(
                            response.errorBody(),
                            response.code()
                        )
                    )
            }
        }

        override fun onFailure(call: Call<ValidateProductResponse>, t: Throwable) {
            qrCodeData.value = Resource.error(ErrorUtils.getError(t))
        }
    }
  /*  fun hitNotValidateQRApi(pincode : String){
        DataRepository.instance.hitCustomerNotValidateProductApi(pincode).enqueue(callbackNotValidQRCodeCheck)
    }
*/
  /*  private val callbackNotValidQRCodeCheck: Callback<ValidateQrcodeResponse> = object :
        Callback<ValidateQrcodeResponse> {
        override fun onResponse(
            call: Call<ValidateQrcodeResponse>,
            response: Response<ValidateQrcodeResponse>
        ) {
            if (response.isSuccessful) {
                notValidQRcode.value = response.body()?.let { Resource.success(it) }
            } else {
                notValidQRcode.value =
                    Resource.error(
                        ErrorUtils.getError(
                            response.errorBody(),
                            response.code()
                        )
                    )
            }
        }

        override fun onFailure(call: Call<ValidateQrcodeResponse>, t: Throwable) {
            notValidQRcode.value = Resource.error(ErrorUtils.getError(t))
        }
    }*/

    fun hitUploadFile(requestBody: MultipartBody) {
        return DataRepository.instance.hitAPIUploadFile(requestBody).enqueue(callbackUploadFile)
    }

    private val callbackUploadFile: Callback<UploadFileResponse> = object :
        Callback<UploadFileResponse> {
        override fun onResponse(
            call: Call<UploadFileResponse>,
            response: Response<UploadFileResponse>
        ) {
            if (response.isSuccessful) {
                invoiceUploadData.value = response.body()?.let { Resource.success(it) }
            } else {
                invoiceUploadData.value =
                    Resource.error(
                        ErrorUtils.getError(
                            response.errorBody(),
                            response.code()
                        )
                    )
            }
        }

        override fun onFailure(call: Call<UploadFileResponse>, t: Throwable) {
            invoiceUploadData.value = Resource.error(ErrorUtils.getError(t))
        }
    }

    /*fun hitSubmitLead(requestBody: JsonObject) {
        return DataRepository.instance.hitSubmitLeadApi(requestBody).enqueue(callbackSubmitLead)
    }*/


    private val callbackSubmitLead: Callback<ValidateProductResponse> = object :
        Callback<ValidateProductResponse> {
        override fun onResponse(
            call: Call<ValidateProductResponse>,
            response: Response<ValidateProductResponse>
        ) {
            if (response.isSuccessful) {
                submitLeadData.value = response.body()?.let { Resource.success(it) }
            } else {
                submitLeadData.value =
                    Resource.error(
                        ErrorUtils.getError(
                            response.errorBody(),
                            response.code()
                        )
                    )
            }
        }

        override fun onFailure(call: Call<ValidateProductResponse>, t: Throwable) {
            submitLeadData.value = Resource.error(ErrorUtils.getError(t))
        }
    }
}