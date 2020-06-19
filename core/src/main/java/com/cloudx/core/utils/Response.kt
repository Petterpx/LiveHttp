@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.cloudx.core.utils

import com.cloudx.core.LiveConfig
import com.cloudx.core.error.ErrorCodeKts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * Created by Petterp
 * on 2020-01-14
 * Function: 资源包装，自定义参考这里
 */
class LiveResponse<out T>(val result: Result, val t: T)

data class Result(val c: Int, val m: String)


inline fun <T> LiveResponse<T>.block(
    noinline blockError: ((Result) -> Unit?)?,
    success: (T) -> Unit
) {

    if (result.c == 200) {
        success(t)
    } else {
//        blockError?.let { it(result) }
//            ?: ErrorCodeKts.getCode(result.c)
    }
}

suspend inline fun <T> LiveResponse<T>.blockMain(
    noinline error: ((Result) -> Unit)? = null,
    noinline success: (T) -> Unit
) {
    withContext(Dispatchers.Main) {
        block(error, success)
    }
}


suspend inline fun <T> LiveResponse<T>.blockIO(
    noinline error: ((Result) -> Unit)? = null,
    noinline success: (T) -> Unit
) {
    withContext(Dispatchers.IO) {
        block(error, success)
    }
}


/**
 * requestBody
 */
inline fun requestBody(obj: () -> Map<String, Any>): RequestBody =
    LiveConfig.config.mGson.toJson(obj()).toRequestBody(
        LiveConfig.config.mediaType
    )





