package com.cloudx.livehttp

import android.widget.Toast
import com.cloudx.core.LiveConfig
import com.cloudx.core.error.ErrorCodeKts
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @Author petterp
 * @Date 2020/6/19-4:04 PM
 * @Email ShiyihuiCloud@163.com
 * @Function 对于Response的扩展函数，很大程度上决定了LiveHttp的使用灵活度
 */


data class WanResponse<out T>(val result: Result, val data: T, val refreshTime: Long)

data class Result(val c: Int, val m: String)



/**
 * 对于同步的扩展
 * 这也是kt 的一大特性，异步代码写成同步形式，其实可以理解为这就是一个逻辑上的处理，suspend在内部是一个标记，具体自查吧
 * */

/** 同步，必须在协程内 */
suspend inline fun <T> WanResponse<T>.block(
    noinline blockError: ((Result) -> Unit?)? = null, isToast: Boolean = true
): T? {
    if (result.c == 200) {
        return data
    } else {
        if (isToast) {
            withContext(Dispatchers.Main) {
                Toast.makeText(LiveConfig.config.mContext, result.m, Toast.LENGTH_SHORT).show()
            }
        }
        //这里优先执行方法内的错误处理
        blockError?.let {
            it(result)
            return null
        }
        //一般情况下，执行的都是全局异常处理，这里也是挂起函数，便于可能存在的耗时操作
        ErrorCodeKts.getCode(result.c)?.let {
            it.obj(result.m)
        }
        return null
    }
}




/**
 * 对于异步回调使用的扩展
 * 不过使用协程好像大家一般不会再去等回调了，但是还是提供了类似的方法，供大家参考
 * */

/** 异步，结果切回main线程,使用场景并不多 */
suspend fun <T> WanResponse<T>.asyncBlockMain(
    isToast: Boolean = true,
    error: ((Result) -> Unit)? = null,
    success: (T) -> Unit
) {
    withContext(Dispatchers.Main) {
        syncBlock(isToast, error, success)
    }
}


/** 异步,结果切回IO线程, */
suspend fun <T> WanResponse<T>.syncBlockIO(
    isToast: Boolean = true,
    error: ((Result) -> Unit)? = null,
    success: (T) -> Unit
) {
    withContext(Dispatchers.IO) {
        syncBlock(isToast, error, success)
    }
}

/** 异步，结果处于默认线程,一般这种情况下，线程都是处于io
 * [success]的回调里可以使用LiveData将数据发射出去
 * */
suspend inline fun <T> WanResponse<T>.syncBlock(
    isToast: Boolean = true, noinline blockError: ((Result) -> Unit?)? = null, success: (T) -> Unit
) {
    if (result.c == 200) {
        success(data)
    } else {
        if (isToast) {
            withContext(Dispatchers.Main) {
                Toast.makeText(LiveConfig.config.mContext, result.m, Toast.LENGTH_SHORT).show()
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
