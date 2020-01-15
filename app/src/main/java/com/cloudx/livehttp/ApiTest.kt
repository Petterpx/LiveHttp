package com.cloudx.livehttp

import com.cloudx.core.utils.LiveResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


/**
 * Created by Petterp
 * on 2020-01-14
 * Function:
 */
interface ApiTest {

    @GET("https://juejin.im/post/5d3c2758f265da1b934e4a8c")
    suspend fun getBaidu(): String

    @GET("https://wanandroid.com/wxarticle/list/408/1/json")
    suspend fun getWan(): LiveResponse<String>

    @POST("/orgframe/root")
    suspend fun login(@Body body: RequestBody): LiveResponse<Login>
}