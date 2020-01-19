package com.cloudx.livehttp

import android.app.Application
import com.cloudx.core.interceptor.LogInterceptor
import com.cloudx.core.LiveConfig

/**
 * Created by Petterp
 * on 2020-01-14
 * Function:
 */
class ContextKtx : Application() {

    override fun onCreate() {
        super.onCreate()
        LiveConfig
            .baseUrl("https://www.wanandroid.com/")
            .context(this@ContextKtx)
            .interCeptor(LogInterceptor())
//        ErrorCodeKts.getCode(101).obj()
    }

}