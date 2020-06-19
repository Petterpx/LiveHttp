package com.cloudx.livehttp

import com.cloudx.core.error.ErrorCodeKts
import com.cloudx.ktx.ui.toast
import com.people.rmxc.fdu.ui.dialog.DialogFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by Petterp
 * on 2020-03-13
 * Function: 自定义Response，符合自己需求
 */
data class PeopleResponse<out T>(val result: Result, val data: T, val refreshTime: Long)

data class Result(val c: Int, val m: String)

/** 同步，io线程，必须在协程体内 */
suspend inline fun <T> PeopleResponse<T>.block(
    isToast: Boolean = true, noinline blockError: ((Result) -> Unit?)? = null
): T? {
    if (result.c == 200) {
        return data
    } else {
        blockError?.let {
            it(result)
            return null
        }
        ErrorCodeKts.getCode(result.c)?.let {
            it.obj(result.m)
        }
        return null
    }
}

/** 异步 */
suspend fun <T> PeopleResponse<T>.syncBlockIO(
    isToast: Boolean = true,
    error: ((Result) -> Unit)? = null,
    success: (T) -> Unit
) {
    withContext(Dispatchers.IO) {
        block(isToast, error, success)
    }
}

/** 异步，默认线程,一般这种情况下，即请求已处于io线程 */
suspend fun <T> PeopleResponse<T>.block(
    isToast: Boolean = true, blockError: ((Result) -> Unit?)? = null, success: (T) -> Unit
) {
    if (result.c == 200) {
        success(data)
    } else {
        DialogFactory.stopLoadingAll()
        if (isToast) {
            withContext(Dispatchers.Main) {
                toast(result.m)
            }
        }
        blockError?.let {
            it(result)
        }
        ErrorCodeKts.getCode(result.c)?.let {
            it.obj(result.m)
        }
    }
}


/** main线程 */
suspend fun <T> PeopleResponse<T>.blockMain(
    isToast: Boolean = true,
    error: ((Result) -> Unit)? = null,
    success: (T) -> Unit
) {
    withContext(Dispatchers.Main) {
        block(isToast, error, success)
    }
}

