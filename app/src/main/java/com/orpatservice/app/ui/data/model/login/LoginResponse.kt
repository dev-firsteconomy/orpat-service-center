package com.orpatservice.app.ui.data.model.login

/**
 * Created by Ajay Yadav on 19/12/21.
 */
data class LoginResponse(val success : Boolean, val data : LoginBaseData, val message : String)

data class LoginBaseData(val token : String, val serviceCenter : ServiceCenter)

data class ServiceCenter(val id : Int, val name : String, val email : String, val mobile : String)
