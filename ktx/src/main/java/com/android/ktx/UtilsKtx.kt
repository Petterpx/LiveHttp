package com.android.ktx

import android.content.Context

/**
 * Created by Petterp
 * on 2020-01-15
 * Function:
 */
fun threadOut() =
    println("当前线程---${Thread.currentThread().name}")