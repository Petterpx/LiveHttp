package com.cloudx.livehttp


import com.google.gson.annotations.SerializedName

data class SystemParent(
    @SerializedName("children")
    val children: List<Any>,
    @SerializedName("courseId")
    val courseId: Int, // 13
    @SerializedName("id")
    val id: Int, // 434
    @SerializedName("name")
    val name: String, // Gityuan
    @SerializedName("order")
    val order: Int, // 190013
    @SerializedName("parentChapterId")
    val parentChapterId: Int, // 407
    @SerializedName("userControlSetTop")
    val userControlSetTop: Boolean, // false
    @SerializedName("visible")
    val visible: Int // 1
)