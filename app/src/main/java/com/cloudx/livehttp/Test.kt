package com.cloudx.livehttp

import com.cloudx.core.error.launchHttp
import kotlinx.coroutines.*
import java.lang.RuntimeException
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis

/**
 * @Author petterp
 * @Date 2020/6/19-8:54 PM
 * @Email ShiyihuiCloud@163.com
 * @Function
 */
fun main() {
    coroutineScope.launch {
       launchHttp {
           somethingPlayGame().start()
           somethingPlayPP().start()
       }
    }
    Thread.sleep(1100)
}

val coroutineScope=CoroutineScope(Dispatchers.Default)


fun somethingPlayGame() = GlobalScope.async {
    delay(1000)
    println("1231231")
}

fun somethingPlayPP() = coroutineScope.async {
    delay(500)
    throw RuntimeException("123123")
}
