package com.cloudx.core

import android.os.Environment
import android.util.Log
import com.cloudx.core.factory.GabonConverterFactory
import com.cloudx.core.interceptor.CacheInterceptor
import com.cloudx.core.interceptor.RequestInterceptor
import com.cloudx.core.utils.LiveConfig
import okhttp3.*
import retrofit2.Retrofit
import java.io.File
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.util.concurrent.TimeUnit

/**
 * Created by Petterp
 * on 2020-01-13
 * Function:
 */

object LiveHttp {
    private val mRetrofit: Retrofit
    private val mOkHttpClient: OkHttpClient
    private val mApiMaps: HashMap<String, Any>


    init {
        // 缓存目录
        val file =
            File(Environment.getExternalStorageDirectory(), "a_cache")
        // 缓存大小
        val cacheSize = 10 * 1024 * 1024
        val builder = OkHttpClient.Builder()
        val liveConfig = LiveConfig.config
        val interceptorList: ArrayList<Interceptor> = liveConfig.mInterceptorList

        //默认开启网络缓存
        if (liveConfig.mIsCache) {
            builder.addInterceptor(CacheInterceptor())
                .addNetworkInterceptor(RequestInterceptor())
                .cache(Cache(liveConfig.mContext.cacheDir, 20 * 1024 * 1024))
        }
        //添加拦截器
        for (interceptor in interceptorList) {
            builder.addInterceptor(interceptor)
        }

        mOkHttpClient = builder.retryOnConnectionFailure(true)
            .connectTimeout(liveConfig.mConnectTimeout, TimeUnit.SECONDS)
            .writeTimeout(liveConfig.mWriteTimeout, TimeUnit.SECONDS)
            .readTimeout(liveConfig.mWriteTimeout, TimeUnit.SECONDS)
            .addNetworkInterceptor(RequestInterceptor())
            .cache(Cache(file, cacheSize.toLong())) // 配置缓存
            .build()

        mRetrofit = Retrofit.Builder()
            .baseUrl(liveConfig.mBaseUrl)
            .client(mOkHttpClient)
            .addConverterFactory(GabonConverterFactory.create(LiveConfig.config.mGson))
            .build()
        mApiMaps = HashMap(10)
    }


    fun <T> createApi(clazz: Class<T>): T {
        val name = clazz.canonicalName
        if (name in mApiMaps) {
            return mApiMaps[name] as T
        }
        val t = mRetrofit.create(clazz)
        if (name != null) {
            mApiMaps += name to t as Any
        }
        return t
    }


    fun <T> createApi(clazz: Class<T>, baseUrl: String): T {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(mOkHttpClient)
            .addConverterFactory(GabonConverterFactory.create(LiveConfig.config.mGson))
            .build()
        return retrofit.create(clazz)
    }


}