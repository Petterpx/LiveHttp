package com.cloudx.core.ktx

import com.cloudx.core.LiveConfig
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * @Author petterp
 * @Date 2020/8/19-6:37 PM
 * @Email ShiyihuiCloud@163.com
 * @Function 一些扩展
 */

/**
 * requestBody
 */
inline fun requestBody(obj: () -> Map<String, Any>): RequestBody =
    LiveConfig.config.mGson.toJson(obj()).toRequestBody(
        LiveConfig.config.mediaType
    )
