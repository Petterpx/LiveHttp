package com.cloudx.core.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.UnsupportedCharsetException

/**
 * Created by Petterp
 * on 2020-01-13
 * Function:
 */

class LogInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val utf8 = Charset.forName("UTF-8")
        // 打印请求报文
        val request = chain.request()
        val requestBody = request.body
        val buffer = Buffer()
        requestBody?.writeTo(buffer)
        val contentType = requestBody?.contentType()
        val charset = contentType?.charset(utf8)
        val reqBody = charset?.let { buffer.readString(it) }
        Log.e(
            "okhttp", String.format(
                "发送请求\nmethod：%s\nurl：%s\nheaders: %s\nbody：%s",
                request.method, request.url, request.headers, reqBody
            )
        )
        // 打印返回报文
        // 先执行请求，才能够获取报文
        val response = chain.proceed(request)
        val responseBody = response.body
        var respBody: String? = null
        if (responseBody != null) {
            val source = responseBody.source()
            source.request(Long.MAX_VALUE)
            val buffer = source.buffer
            var charset = utf8
            val contentType = responseBody.contentType()
            if (contentType != null) {
                try {
                    charset = contentType.charset(utf8)
                } catch (e: UnsupportedCharsetException) {
                    e.printStackTrace()
                }
            }
            respBody = buffer.clone().readString(charset)
        }
        Log.e(
            "okhttp", String.format(
                "收到响应\n%s %s\n请求url：%s\n请求body：%s\n响应body：%s",
                response.code, response.message, response.request.url, reqBody, respBody
            )
        )
        return response
    }
}
