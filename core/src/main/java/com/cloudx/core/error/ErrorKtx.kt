package com.cloudx.core.error

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * Retrofit异常处理
 * //参考自掘金
 */
private suspend fun tryCatch(
    catchBlock: suspend (e: Throwable) -> Unit,
    finalBlock: suspend () -> Unit,
    tryBlock: suspend CoroutineScope.() -> Unit
) {
    try {
        coroutineScope {
            tryBlock()
        }
    } catch (e: Throwable) {
        catchBlock(e)
    } finally {
        finalBlock()
    }
}

/** 适用于ViewModel的http作用域 */
suspend fun ViewModel.launchHttp(
    coroutineContext: CoroutineContext = Dispatchers.IO,
    finalBlock: suspend () -> Unit = {},
    errorBlock: suspend (e: Throwable) -> Unit = {},
    tryBlock: suspend CoroutineScope.() -> Unit
) {
    viewModelScope.launch(coroutineContext) {
        tryCatch(errorBlock, finalBlock, tryBlock)
    }
}

/** 适用于协程范围的http作用域,作用域取决与内部*/
suspend fun CoroutineScope.launchHttp(
    finalBlock: suspend () -> Unit = {},
    errorBlock: suspend (e: Throwable) -> Unit = {},
    tryBlock: suspend CoroutineScope.() -> Unit
) {
    tryCatch(errorBlock, finalBlock, tryBlock)
}


/** 适用于普通情况下，为什么这里没有自己开协程呢，主要原因是为了便于*/
fun LifecycleOwner.launchHttp(
    context: CoroutineContext = Dispatchers.IO,
    finalBlock: suspend () -> Unit = {},
    errorBlock: suspend (e: Throwable) -> Unit = {},
    tryBlock: suspend CoroutineScope.() -> Unit
) {
    lifecycleScope.launch(context) {
        tryCatch(errorBlock, finalBlock, tryBlock)
    }
}

/** 适用于普通情况下，为什么这里没有自己开协程呢，主要原因是为了便于*/
suspend fun launchHttp(
    finalBlock: suspend () -> Unit = {},
    errorBlock: suspend (e: Throwable) -> Unit = {},
    tryBlock: suspend CoroutineScope.() -> Unit
) {
    tryCatch(errorBlock, finalBlock, tryBlock)
}
