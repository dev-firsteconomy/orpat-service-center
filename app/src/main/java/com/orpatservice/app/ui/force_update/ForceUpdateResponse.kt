package com.orpatservice.app.ui.force_update

class ForceUpdateResponse (
    val message: String,
    var data    : Data,
    val success: Boolean,

    )
data class Data (
    val is_force_update: Boolean,
)
