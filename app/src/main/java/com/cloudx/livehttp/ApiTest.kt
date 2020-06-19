package com.cloudx.livehttp

import retrofit2.http.GET

/**
 * @Author petterp
 * @Date 2020/6/19-4:27 PM
 * @Email ShiyihuiCloud@163.com
 * @Function
 */
interface ApiTest {
    @GET("https://www.baidu.com/")
    suspend fun getBaiDu(): String

    @GET("https://www.baidu.com/")
    suspend fun getBai(): WanResponse<String>
}