package com.cloudx.livehttp

import retrofit2.Call
import retrofit2.http.GET


/**
 * Created by Petterp
 * on 2020-01-14
 * Function:
 */
interface ApiTest {

    @GET("https://juejin.im/post/5d3c2758f265da1b934e4a8c")
    suspend fun getBaidu(): String
}