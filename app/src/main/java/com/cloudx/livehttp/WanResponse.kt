package com.cloudx.livehttp

import android.widget.Toast
import com.cloudx.core.LiveConfig
import com.cloudx.core.error.ErrorCodeKts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by Petterp
 * on 2020-03-13
 * Function: 自定义wanResponse，符合自己需求
 */
data class WanResponse<out T>(val result: Result, val data: T, val refreshTime: Long)

data class Result(val c: Int, val m: String)

