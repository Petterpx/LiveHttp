package com.cloudx.livehttp

import android.app.Application
import com.cloudx.core.utils.LiveConfig

/**
 * Created by Petterp
 * on 2020-01-14
 * Function:
 */
class ContextKtx : Application() {

    companion object {
        val context = this
    }

    override fun onCreate() {
        super.onCreate()
        LiveConfig.config
            .baseUrl("https://www.jianshu.com/p/a900ee71ae7f/")
            .context(this)
    }
}