package com.deora.cms_app.network

import com.deora.cms_app.contact.Request
import com.deora.cms_app.contact.Response
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface IRequestContract {
    @POST("contact.php")
    fun makeApiCall(@Body request:Request): Call<Response>
}