package com.deora.cms_app.contact

data class Response(
    var status: Boolean = false,
    var responseCode: Int = -1,
    var message: String = "",
    var id: String = "",
    var contacts: MutableList<Contact> = mutableListOf()
)
