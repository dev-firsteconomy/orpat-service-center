package com.orpatservice.app.data.remote

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.orpatservice.app.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object APIClient {

    var gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    private var okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthTokenInterceptor())
        .readTimeout(3, TimeUnit.MINUTES)
        .writeTimeout(3, TimeUnit.MINUTES)
        .connectTimeout(3, TimeUnit.MINUTES)
        .build()

    private val client: Retrofit
        get() = Retrofit.Builder()
            .baseUrl(BuildConfig.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    private val auth_client: Retrofit
        get() = Retrofit.Builder()
            .baseUrl(BuildConfig.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()

    fun apiInterface(): ApiEndPoint {
        return client.create<ApiEndPoint>(ApiEndPoint::class.java)
    }

    fun apiAuthInterface(): ApiEndPoint {
        return auth_client.create<ApiEndPoint>(ApiEndPoint::class.java)
    }
}

class AuthTokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
          //  .header("Authorization", "Bearer " + prefs.jwtToken)
            .header(Constants.PLATFORM_KEY, Constants.ANDROID_KEY)
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
