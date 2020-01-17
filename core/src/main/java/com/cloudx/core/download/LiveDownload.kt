package com.cloudx.core.download

import com.cloudx.core.utils.LiveConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * Created by Petterp
 * on 2020-01-17
 * Function:
 */
object LiveDownload {
    val mRetrofit: Retrofit
    private val mOkHttpClient: OkHttpClient

    init {
        val liveConfig = LiveConfig.config
        val builder = OkHttpClient.Builder()
        mOkHttpClient = builder.retryOnConnectionFailure(true)
            .connectTimeout(liveConfig.mConnectTimeout, TimeUnit.SECONDS)
            .writeTimeout(liveConfig.mWriteTimeout, TimeUnit.SECONDS)
            .readTimeout(liveConfig.mWriteTimeout, TimeUnit.SECONDS)
            .build()

        mRetrofit = Retrofit.Builder()
            .baseUrl(LiveConfig.config.mBaseUrl)
            .client(mOkHttpClient)
            .build()
    }


}