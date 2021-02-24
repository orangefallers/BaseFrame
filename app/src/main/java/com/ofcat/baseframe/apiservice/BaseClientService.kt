package com.ofcat.baseframe.apiservice

import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import com.ofcat.baseframe.model.BaseDto
import com.ofcat.baseframe.tool.ConditionTool
import com.ofcat.baseframe.tool.DateTool.DATE_FORMATTER
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import okhttp3.logging.HttpLoggingInterceptor.Logger
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

open class BaseClientService protected constructor() {

    companion object {
        private const val TIMEOUT = 30L
        private val SHOW_SYSTEM_MSG = false
        private val TAG = BaseClientService::class.java.simpleName
        private val GSON = GsonBuilder()
            .enableComplexMapKeySerialization()
            .disableHtmlEscaping()
            .setDateFormat(DATE_FORMATTER)
            .setPrettyPrinting()
            .serializeNulls()
            .setLenient()
            .create()
    }

    fun applyClientService(baseUrl: String): Retrofit {
        // Create logging interceptor
        val interceptor = HttpLoggingInterceptor(Logger { applyHttpMessage(it) })
        interceptor.level = if (SHOW_SYSTEM_MSG) Level.BODY else Level.NONE

        // Create ok http client builder
        val builder = OkHttpClient.Builder()
            .followRedirects(true)
            .followSslRedirects(true)
            .retryOnConnectionFailure(true)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .addNetworkInterceptor(interceptor)
//        applySslSocketFactory(builder)

        // Build ok http client
        val client = builder.build()

        // Build retrofit
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GSON))
            .client(client)
            .build()
    }

    private fun applyHttpMessage(log: String) {
        when {
            !SHOW_SYSTEM_MSG -> {

            }
            (log.isNotEmpty() && log.contains("-->") && log.contains("=")) -> {
                com.orhanobut.logger.Logger.w(log)
            }
            (log.isNotEmpty() && log.contains("<--") && log.contains("https://")) -> {
                com.orhanobut.logger.Logger.i(log)
            }
            (log.isNotEmpty() && ConditionTool.isJsonStr(log)) -> {
                com.orhanobut.logger.Logger.json(log)
            }
            else -> {
                com.orhanobut.logger.Logger.i(TAG, "client service log = $log")
            }
        }
    }

//    private fun applySslSocketFactory(builder: OkHttpClient.Builder): OkHttpClient.Builder {
//        if (Build.VERSION.SDK_INT in 19..21) {
//            try {
//                val sslContext = SSLContext.getInstance("TLSv1.2")
//                sslContext.init(null, null, null)
//
//                val specs = ArrayList<ConnectionSpec>()
//                specs.add(ConnectionSpec.Builder(
//                    ConnectionSpec.MODERN_TLS).tlsVersions(TlsVersion.TLS_1_2).build())
//                specs.add(ConnectionSpec.COMPATIBLE_TLS)
//                specs.add(ConnectionSpec.CLEARTEXT)
//
//                builder.sslSocketFactory(TLS12SocketFactory(sslContext.socketFactory))
//                builder.connectionSpecs(specs)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//        return builder
//    }


    inline fun <reified T : BaseDto<*>> applySubscribeVo(): ObservableTransformer<Response<T>, T> {
        return ObservableTransformer { observable ->
            observable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map { response ->
                    if (response.isSuccessful.not()) {
                        // Generate vo with response information
                        val errorVo = T::class.java.newInstance()
                        errorVo.code = response.code().toString()
                        errorVo.message = response.message()
                        errorVo.apiName = response.raw().request().url().uri().path
                        // Generate vo with error body
                        response.errorBody()?.let { errorBody ->
                            val errorObject = try {
                                JSONObject(errorBody.string())
                            } catch (ex: Throwable) {
                                JSONObject()
                            }

                            // generic return code
                            if (errorObject.has("code")) {
                                errorVo.code = errorObject.getString("code")
                            }
                            // generic return message
                            if (errorObject.has("message")) {
                                errorVo.message = errorObject.getString("message")
                            }
                            // api/public return code
                            if (errorObject.has("ret")) {
                                errorVo.code = errorObject.getString("ret")
                            }
                            // api/public return message
                            if (errorObject.has("msg")) {
                                errorVo.message = errorObject.getString("msg")
                            }
                        }

                        return@map errorVo
                    }

                    // Response vo
                    val vo = response.body() ?: T::class.java.newInstance()

                    return@map vo
                }
                .onErrorReturn {
                    // Generate error vo
                    val errorVo = T::class.java.newInstance()

                    // Error message
                    errorVo.message = it.message

                    // Error code
                    when (it) {
                        is SocketTimeoutException -> {
                        }
                        is JsonParseException -> {
                        }
                        is IOException -> {
                        }
                        else -> {
                        }
                    }

                    return@onErrorReturn errorVo
                }
        }
    }
}