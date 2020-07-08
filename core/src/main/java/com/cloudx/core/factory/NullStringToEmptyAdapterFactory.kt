package com.cloudx.core.factory

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken


/**
 * @Author petterp
 * @Date 2020/7/8-12:09 PM
 * @Email ShiyihuiCloud@163.com
 * @Function
 */
class NullStringToEmptyAdapterFactory : TypeAdapterFactory {
    override fun <T> create(gson: Gson?, type: TypeToken<T>): TypeAdapter<T>? {
        val rawType = type.rawType as Class<T>
        return if (rawType != String::class.java) {
            null
        } else StringAdapter() as TypeAdapter<T>
    }
}
