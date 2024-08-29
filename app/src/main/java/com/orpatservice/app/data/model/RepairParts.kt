package com.orpatservice.app.data.model

data class RepairParts(
    val id: Int,
    val name: String,
    val category_name: String,
    val sub_category_name: String,
    val child_category_name: String,
    val product_name: String
){
    override fun toString(): String {
        return name
    }
}
