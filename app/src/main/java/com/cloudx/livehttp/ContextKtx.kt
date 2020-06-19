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
        //无需再去配置context，内部已经使用了start App组件
        LiveConfig
            .baseUrl("https://www.wanandroid.com/")
//            .interceptor(LogInterceptor())
//            .log()
    }

}