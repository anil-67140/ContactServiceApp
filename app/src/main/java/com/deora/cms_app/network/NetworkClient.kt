package com.deora.cms_app.network

import com.deora.cms_app.utils.Configuration
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkClient {
    private var networkClient: Retrofit? = null

    private fun getNetworkClient(): Retrofit {
        return networkClient ?: synchronized(this) {
            networkClient ?: Retrofit.Builder()
                .baseUrl(Configuration.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().also { networkClient = it }
        }
    }

    val api: IRequestContract by lazy {
        getNetworkClient().create(IRequestContract::class.java)
    }
}
