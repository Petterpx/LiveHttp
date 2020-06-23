package com.cloudx.livehttp

import android.app.Application
import android.util.Log
import android.widget.Toast
import com.cloudx.core.interceptor.LogInterceptor
import com.cloudx.core.LiveConfig
import com.cloudx.core.error.EnumException
import com.cloudx.core.net.INetEnable
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
            .baseUrl("https://www.wanandroid.com/")
            .errorAppKtx(EnumException.NET_DISCONNECT) {
                withContext(Dispatchers.Main){
                    Toast.makeText(this@ContextKtx, "网络断开", Toast.LENGTH_SHORT).show()
                }
                Log.e("petterp", "")
            }
            .errorAppKtx(EnumException.NET_UNAVAILABLE){
                withContext(Dispatchers.Main){
                    Toast.makeText(this@ContextKtx, "网络不可用", Toast.LENGTH_SHORT).show()

                }


            } .errorAppKtx(EnumException.CONNECT_EXCEPTION){
                withContext(Dispatchers.Main){
                    Toast.makeText(this@ContextKtx, "连接超时", Toast.LENGTH_SHORT).show()

                }

            }
            .netObserListener(object :INetEnable{
                override fun netOpen() {
                    Toast.makeText(this@ContextKtx, "打开", Toast.LENGTH_SHORT).show()
                }

                override fun netOff() {
                    Toast.makeText(this@ContextKtx, "guanbi", Toast.LENGTH_SHORT).show()

                }

            })
//            .interceptor(LogInterceptor())
//            .log()
    }

}