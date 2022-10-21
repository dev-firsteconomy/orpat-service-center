package com.orpatservice.app.data.model.login

import com.orpatservice.app.data.model.PaginationData
import com.orpatservice.app.data.model.TechnicianData

data class SendHappyCodeResponse (
        val success: Boolean,
        //val data: TechnicianBaseData,
        val message: String
    )

    data class TechnicianBaseData(
        val data: ArrayList<TechnicianData>,
        val pagination: PaginationData
    )

