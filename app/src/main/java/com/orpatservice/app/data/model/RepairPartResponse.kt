package com.orpatservice.app.data.model

data class RepairPartResponse(val success: Boolean,
                              val data: ArrayList<RepairParts>,
                              val message: String)
