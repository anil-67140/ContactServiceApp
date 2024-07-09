package com.deora.cms_app.contact

import com.google.gson.annotations.SerializedName

data class Contact(
    @SerializedName("Id") var id: String = "",
    @SerializedName("Name") var name: String = "",
    @SerializedName("MobileNo") var mobileNo: String = "",
    @SerializedName("Email") var email: String = ""
)
