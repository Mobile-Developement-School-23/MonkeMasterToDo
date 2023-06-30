package com.monke.yandextodo.data.api

import com.monke.yandextodo.domain.Constants
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request().newBuilder()
            .addHeader(
                "Authorization",
                "Bearer " + Constants.BEARER_TOKEN).build()
        return chain.proceed(request)
    }


}