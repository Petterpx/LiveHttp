package com.cloudx.livehttp

import com.android.ktx.getArg
import com.android.ktx.getArgSet
import com.android.ktx.putArg
import com.android.ktx.putArgSet
import com.cloudx.core.utils.LiveConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.*

/**
 * Created by Petterp
 * on 2020-01-16
 * Function: 网络拦截器
 */
/**
 * 接受cookie拦截器
 */
class ReceivedCookiesInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())
        if (originalResponse.headers("Set-Cookie").isNotEmpty()) {
            val cookiesSet =
                HashSet(originalResponse.headers("Set-Cookie"))
            putArgSet(LiveConfig.config.mContext, cookiesSet)
        }
        return originalResponse
    }
}

/**
 * 添加header包含cookie拦截器
 */
class AddHeaderInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        //添加cookie
        val cookieSet: HashSet<String> = getArgSet(LiveConfig.config.mContext)
        for (cookie in cookieSet) {
            builder.addHeader("Cookie", cookie)
        }
        val userToken: String = getArg(LiveConfig.config.mContext)
        if (userToken.isNotEmpty()) {
            builder.addHeader("token", getArg(LiveConfig.config.mContext))
        }
        return chain.proceed(builder.build())
    }
}
