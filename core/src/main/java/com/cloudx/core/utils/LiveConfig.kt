package com.cloudx.core.utils

import android.content.Context
import com.cloudx.core.interceptor.LogInterceptor
import com.cloudx.core.interceptor.RequestInterceptor
import okhttp3.Interceptor

/**
 * Created by Petterp
 * on 2020-01-13
 * Function:
 */
class LiveConfig {

    companion object {
        val config = LiveConfig()
    }

    lateinit var mContext: Context
    var mIsCache = true
    var mWriteTimeout = 30L
    var mConnectTimeout = 10L
    lateinit var mBaseUrl: String
    var mInterceptorList: ArrayList<Interceptor>

    init {
        mInterceptorList = ArrayList(5)
    }

    /**
     *  初始化
     */
    fun initDefault(context: Context, url: String) {
        mContext = context
        mBaseUrl = url
        mInterceptorList.add(LogInterceptor())

    }

    fun baseUrl(url: String): LiveConfig {
        mBaseUrl = url
        return this
    }

    fun context(context: Context): LiveConfig {
        mContext = context
        return this
    }

    fun connectTimeout(time: Long): LiveConfig {
        mConnectTimeout = time
        return this
    }

    fun WriteTimeout(time: Long): LiveConfig {
        mWriteTimeout = time
        return this
    }

    fun isCache(cache: Boolean): LiveConfig {
        mIsCache = cache
        if (mIsCache) mInterceptorList.add(RequestInterceptor())
        return this
    }

    fun listInterCeptor(list: ArrayList<Interceptor>): LiveConfig {
        mInterceptorList = list
        return this
    }


}

