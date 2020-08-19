package com.cloudx.livehttp

import android.app.Application
import android.util.Log
import com.cloudx.core.LiveConfig
import com.cloudx.core.error.CodeBean
import com.cloudx.core.error.EnumException
import com.cloudx.core.net.INetEnable

/**
 * Created by Petterp
 * on 2020-01-14
 * Function:
 */
class ContextKtx : Application() {
    override fun onCreate() {
        super.onCreate()
        LiveConfig
            .context(this)
            .baseUrl("https://www.baidu.com")   //baseUrl
            .writeTimeout(30L)     //设置读写超时时间，默认30l
            .connectTimeout(10L)  //设置连接超时时间,默认10l
            .isCache(true)     //开启网络缓存，默认关闭
            .filePath("xxx")    //文件下载路径
            .log()   //需要导入 logger 依赖
            .initNetObser()        //监听网络,必须在context之后
            .initNetObser(object : INetEnable {
                override fun netOpen() {
                    //网络打开
                }

                override fun netOff() {
                    //网络关闭
                }

            })
            //以下错误逻用于全局请求码处理,具体处理方式皆处于挂起函数
            .errorCodeKtx(101, CodeBean {
                //默认为发起网络请求的线程，一般为io,记得调用withContext()
                //对于code=101的处理
            })
            //通用网络处理方案
            .universalErrorHttpKtx {
                //EnumException.CONNECT_EXCEPTION
                //EnumException.TIMEOUT_EXCEPTION
                //EnumException.SOCKET_EXCEPTION,
                //EnumException.NET_UNAVAILABLE,
                Log.e("petterp", "通用网络连接-超时等失败，如网络开启，但网络其实不可用等情况")
            }
            //以下错误用于网络异常处理，具体处理域皆处于挂起函数
            .errorHttpKtx(EnumException.NET_DISCONNECT) {
                Log.e("petterp", "网络断开")
            }
//
    }

}