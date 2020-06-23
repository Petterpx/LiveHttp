package com.cloudx.livehttp

import android.app.Application
import android.util.Log
import android.widget.Toast
import com.cloudx.core.interceptor.LogInterceptor
import com.cloudx.core.LiveConfig
import com.cloudx.core.error.CodeBean
import com.cloudx.core.error.EnumException
import com.cloudx.core.net.INetEnable
import com.cloudx.core.net.NetObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
            .baseUrl("https://www.baidu.com")   //baseUrl
            .writeTimeout(30L)     //设置读写超时时间，默认30l
            .connectTimeout(10L)  //设置连接超时时间,默认10l
            .isCache(true)     //开启网络缓存，默认关闭
            .filePath("xxx")    //文件下载路径

            //以下错误逻用于全局请求码处理,具体处理方式皆处于挂起函数
            .errorCodeKtx(101, CodeBean {
                //默认为发起网络请求的线程，一般为io,记得调用withContext()
                //对于code=101的处理
            })
//            .errorCodeKtx(SparseArray<CodeBean>())  //添加批量错误逻辑处理


            //以下错误用于网络异常处理，具体处理方式皆处于挂起函数
            .errorHttpKtx(EnumException.NET_DISCONNECT) {
                Log.e("petterp", "网络断开")
            }
            .universalErrorHttpKtx {
                //便于快速处理常用网络报错
                Log.e("petterp", "通用网络连接-超时等失败，如网络开启，但网络其实不可用等情况")
            }
            .netObserListener(object : INetEnable {
                override fun netOpen() {
                    //注意，这里并没有ping,至于网络是否真的可用，没有做处理，如需判断，请在子线程调用。
                    //框架内部已经处理了网络是否可用的异常，具体查看 ErrorHttpKtx类
                    // NetObserver.isAvailable() 此方法会去ping
                    Log.e("petterp", "网络打开")
                }

                override fun netOff() {
                    Log.e("petterp", "网络关闭")
                }

            })
            .log()

    }

}