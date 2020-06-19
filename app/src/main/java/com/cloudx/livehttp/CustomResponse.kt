package com.cloudx.livehttp

import com.cloudx.core.error.ErrorCodeKts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by Petterp
 * on 2020-01-19
 * Function: 自定义资源包装-玩android
 */
data class WanResponse<out T>(val data: T, val errorCode: Int, val errorMsg: String)


inline fun <T> WanResponse<T>.block(
    noinline blockError: ((Pair<Int, String>) -> Unit)? = null,
    success: (T) -> Unit
) {
    if (errorCode == 0) {
        data?.let { success(it) }
    } else {
        blockError?.let { it(errorCode to errorMsg) }
            ?: ErrorCodeKts.getCode(errorCode)?.apply {
                obj(errorMsg)
            }
    }
}

suspend inline fun <T> WanResponse<T>.blockMain(
    noinline error: ((Pair<Int, String>) -> Unit)? = null,
    noinline success: (T) -> Unit
) {
    withContext(Dispatchers.Main) {
        block(error, success)
    }
}

fun <T> WanResponse<T>.blockData(): T? {
    if (errorCode == 0) {
        return data
    }
    return null
}


suspend inline fun <T> WanResponse<T>.blockIO(
    noinline error: ((Pair<Int, String>) -> Unit)? = null,
    noinline success: (T) -> Unit
) {
    withContext(Dispatchers.IO) {
        block(error, success)
    }
}