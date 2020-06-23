package com.cloudx.core

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import android.util.SparseArray
import com.cloudx.core.error.CodeBean
import com.cloudx.core.error.EnumException
import com.cloudx.core.error.ErrorCodeKts
import com.cloudx.core.interceptor.LiveLog
import com.cloudx.core.interceptor.RequestInterceptor
import com.cloudx.core.net.INetEnable
import com.cloudx.core.net.NetObserver
import com.google.gson.Gson
import kotlinx.coroutines.coroutineScope
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File

/**
 * Created by Petterp
 * on 2020-01-13
 * Function: 配置信息
 */
object LiveConfig {
    val config = Config()

    class Config {
        lateinit var mContext: Context
        var mIsCache = false
        var mWriteTimeout = 10L
        var mConnectTimeout = 15L
        lateinit var mBaseUrl: String
        var mInterceptorList: ArrayList<Interceptor> = ArrayList(5)
        val mGson = Gson()
        val mediaType = "application/json;charset=UTF-8".toMediaTypeOrNull()

        @SuppressLint("NewApi")
        var downloadName = ""
    }


    /** 用于 Start-APP 初始化，默认会初始化存储路径 */
    internal fun initContext(context: Context) {
        config.mContext = context
        config.downloadName = context.packageName
    }

    fun log(): LiveConfig {
        LiveLog.init()
        return this
    }


    /** baseUrl */
    fun baseUrl(url: String): LiveConfig {
        config.mBaseUrl = url
        return this
    }

    fun connectTimeout(time: Long): LiveConfig {
        config.mConnectTimeout = time
        return this
    }

    fun writeTimeout(time: Long): LiveConfig {
        config.mWriteTimeout = time
        return this
    }

    fun isCache(cache: Boolean): LiveConfig {
        config.mIsCache = cache
        if (config.mIsCache) config.mInterceptorList.add(RequestInterceptor())
        return this
    }

    fun interceptors(list: ArrayList<Interceptor>): LiveConfig {
        config.mInterceptorList = list
        return this
    }

    fun interceptor(interceptor: Interceptor): LiveConfig {
        config.mInterceptorList.add(interceptor)
        return this
    }

    fun netObserListener(listener: INetEnable): LiveConfig {
        NetObserver.iNetEnable = listener
        return this
    }

    /** 请求错误码处理 */
    fun errorKtx(code: Int, codeBean: CodeBean): LiveConfig {
        ErrorCodeKts.putCode(code, codeBean)
        return this
    }

    /** 请求错误码处理 */
    fun errorKtx(sprArray: SparseArray<CodeBean>): LiveConfig {
        ErrorCodeKts.putCodeAll(sprArray)
        return this
    }

    /** 网络异常处理 */
    fun errorAppKtx(vararg enumException: EnumException, obj: suspend () -> Unit): LiveConfig {
        for (it in enumException) {
            ErrorCodeKts.putError(it, obj)
        }
        return this
    }

    fun filePath(path: String): LiveConfig {
        config.downloadName = path
        return this
    }

}
