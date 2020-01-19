package com.cloudx.livehttp

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.*


/**
 * Created by Petterp
 * on 2020-01-14
 * Function:
 */
interface ApiTest {

    @GET("https://juejin.im/post/5d3c2758f265da1b934e4a8c")
    suspend fun getBaidu(): String

    @GET("/wxarticle/chapters/json")
    suspend fun getWan(): WanResponse<List<SystemParent>>


    @Multipart
    @POST("/upload/head")
    suspend fun uploadFile(@Part file: MultipartBody.Part): String

    @Multipart
    @POST("")
    suspend fun uploadFiles(@Part files: List<MultipartBody.Part>): String

    @Streaming
    @GET("https://tva1.sinaimg.cn/large/006tNbRwly1gaxcpmvvuxj30n006n3zv.jpg")
    suspend fun dowload(): ResponseBody

    @Streaming
    @GET
    suspend fun dowload(@Url url: String): ResponseBody
}