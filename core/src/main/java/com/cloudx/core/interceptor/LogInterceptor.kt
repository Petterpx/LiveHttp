package com.cloudx.core.interceptor

import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.lang.Exception
import java.nio.charset.Charset


/**
 * Created by Petterp
 * on 2020-01-17
 * Function: 日志拦截，（不响应文件下载body）
 */
class LogInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val response = chain.proceed(request)
        val utf8 = Charset.forName("UTF-8")
        val reqBody = request.body?.let {
            it.contentType()?.charset(utf8)?.let { it1 -> Buffer().readString(it1) }
        }
        with(request) {
            Log.e(
                "livehttp", String.format(
                    "发送请求\nmethod：%s\nurl：%s\nheaders: %s\nbody：%s",
                    method, url, request.headers, reqBody
                )
            )

        }


        try {
            //如果是文件，需要处理一下
            response.body?.let {
                if (it.contentLength() == -1L) {
                    val source = it.source()
                    source.request(Long.MAX_VALUE) // Buffer the entire body.
                    val buffer: Buffer = source.buffer
                    val charset: Charset = Charset.forName("UTF-8")
                    val body = buffer.clone().readString(charset)
                    with(response) {
                        Log.e(
                            "livehttp", String.format(
                                "收到响应\n响应码%s\n请求url：%s\n请求body：%s\n返回body：%s",
                                code, message, request.url, body
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("livehttp", "logInterceptor-Error-${e.message}")
        }
        return response

    }

}