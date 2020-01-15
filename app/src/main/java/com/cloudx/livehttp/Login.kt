package com.cloudx.livehttp


import com.google.gson.annotations.SerializedName

data class Login(
    @SerializedName("data")
    val `data`: Data
) {
    data class Data(
        @SerializedName("oauthToken")
        val oauthToken: String, // token:a6c780672ead40cfbe6e53767a56ed16:6c6b06d9615f411b91d1467b174f1727
        @SerializedName("rongCloudToken")
        val rongCloudToken: String, // iv1yciAU9dUfvPmfP84SQ7xUxfBoYjnvYKmdCV/XxGK13wnYZdDsdR0vgfm8MOPIXqEZtYmNPWaUWM+0uXwdTwHhzIoCH4OpP0HnYRPg7ExPbTl6vVhkP0kFeNWorfxx
        @SerializedName("userHeadUrl")
        val userHeadUrl: String, // https://pet-im-online.oss-cn-beijing.aliyuncs.com/app/user/head/default/default.jpg
        @SerializedName("userId")
        val userId: String, // a6c780672ead40cfbe6e53767a56ed16
        @SerializedName("userName")
        val userName: String, // 用户9913159
        @SerializedName("userToken")
        val userToken: String // token:a6c780672ead40cfbe6e53767a56ed16:6c6b06d9615f411b91d1467b174f1727
    )
}