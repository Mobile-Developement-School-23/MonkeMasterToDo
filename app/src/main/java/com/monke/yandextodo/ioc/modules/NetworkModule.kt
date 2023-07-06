package com.monke.yandextodo.ioc.modules

import com.monke.yandextodo.data.networkService.interceptors.TokenInterceptor
import com.monke.yandextodo.domain.Constants
import com.monke.yandextodo.ioc.scopes.AppScope
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @AppScope
    @Provides
    fun provideRetrofitClient(okHttpClient: OkHttpClient) = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @AppScope
    @Provides
    fun provideOkHttpClient() = OkHttpClient.Builder()
        .addInterceptor(TokenInterceptor()).build()
}