package com.cloudx.core

import android.content.Context
import androidx.startup.Initializer
import com.cloudx.core.LiveConfig
import com.cloudx.core.LiveHttp

/**
 * @Author petterp
 * @Date 2020/6/16-4:23 PM
 * @Email ShiyihuiCloud@163.com
 * @Function LiveHttp的初始化文件
 */
class LiveHttpInitializer : Initializer<LiveConfig> {
    override fun create(context: Context): LiveConfig {
        LiveConfig.initContext(context)
        return LiveConfig
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> = mutableListOf()
}