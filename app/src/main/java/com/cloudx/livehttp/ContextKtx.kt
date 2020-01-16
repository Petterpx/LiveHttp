package com.cloudx.livehttp

import android.app.Application
import com.cloudx.core.error.CodeBean
import com.cloudx.core.error.ErrorCodeKts
import com.cloudx.core.interceptor.LogInterceptor
import com.cloudx.core.utils.LiveConfig
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Created by Petterp
 * on 2020-01-14
 * Function:
 */
class ContextKtx : Application() {

    override fun onCreate() {
        super.onCreate()
        LiveConfig
            .baseUrl("https://qingfeng.rmny.com.cn:18030/")
            .context(this@ContextKtx)
//            .errorKtx(101, CodeBean("asda"))
            .interCeptor(ReceivedCookiesInterceptor())
            .interCeptor(AddHeaderInterceptor())
            .interCeptor(LogInterceptor())
//        ErrorCodeKts.getCode(101).obj()
    }

}