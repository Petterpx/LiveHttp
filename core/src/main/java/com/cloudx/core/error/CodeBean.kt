package com.cloudx.core.error

/**
 * Created by Petterp
 * on 2020-01-14
 * Function: 异常处理方法
 */
data class CodeBean(val message: String, val obj: () -> Boolean = { true })