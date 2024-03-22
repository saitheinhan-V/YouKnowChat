package com.chat.youknow.network

import android.text.TextUtils
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

object RequestBuilder {

    private lateinit var retrofit: Retrofit
    private lateinit var builder: Retrofit.Builder


    private fun createOkHttp(): OkHttpClient {
        val trustManager = object : X509TrustManager {
            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) =
                Unit

            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) =
                Unit

            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
        }
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, arrayOf(trustManager), java.security.SecureRandom())

        return OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.MINUTES)
            .callTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .sslSocketFactory(sslContext.socketFactory, trustManager)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    fun <S> createService(serviceClass: Class<S>, baseUrl: String): S {
        return createService(serviceClass, baseUrl, "")
    }

    fun <S> createService(serviceClass: Class<S>, baseUrl: String, token: String?): S {
        builder = Retrofit.Builder()
            .client(createOkHttp())
            .baseUrl(baseUrl)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())

        retrofit = builder.build()

        if (!TextUtils.isEmpty(token)){
            val interceptor = AuthenticationInterceptor(token!!)
            val client = OkHttpClient.Builder()
            if (!client.interceptors().contains(interceptor)) {
                client.addInterceptor(interceptor)
                builder.client(client.build())
            }
            retrofit = builder.build()
        }

        return retrofit.create(serviceClass)
    }
}