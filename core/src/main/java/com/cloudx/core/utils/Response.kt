package com.cloudx.core.utils

import com.cloudx.core.error.ErrorCodeKts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * Created by Petterp
 * on 2020-01-14
 * Function:
 */
class LiveResponse<T>(val result: Result, val t: T)

class Result(var c: Int, var m: String)


inline fun <T> LiveResponse<T>.block(noinline blockError: ((Result) -> Unit)? = null, success: (T) -> Unit) {
    if (result.c == 200) {
        success(t)
    } else {
        blockError?.let { it(result) } ?: ErrorCodeKts.getCode(result.c).obj.invoke()
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


fun requestBody(obj: () -> Map<String, Any>): RequestBody =
    LiveConfig.mGson.toJson(obj).toRequestBody(LiveConfig.mediaType)