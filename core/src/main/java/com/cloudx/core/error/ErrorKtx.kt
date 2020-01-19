package com.cloudx.core.error

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope

/**
 * Retrofit异常处理
 * //参考自掘金
 */
suspend fun tryCatch(
    catchBlock: suspend CoroutineScope.(e: Throwable) -> Unit = {},
    finalBlock: suspend CoroutineScope.() -> Unit = {},
    tryBlock: suspend CoroutineScope.() -> Unit
) {
    coroutineScope {
        try {
            tryBlock()
        } catch (e: Throwable) {
            catchBlock(e)
        } finally {
            finalBlock()
        }
    }
}

/**
 *  发起一个网络请求
 */
 suspend fun launchHttp(
    finalBlock: suspend CoroutineScope.() -> Unit = {},
    errorBlock: suspend CoroutineScope.(e: Throwable) -> Unit = {},
    tryBlock: suspend CoroutineScope.() -> Unit
) {
    tryCatch(errorBlock, finalBlock, tryBlock)
}

