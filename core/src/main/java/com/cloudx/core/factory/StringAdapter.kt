package com.cloudx.core.factory

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import okio.IOException


/**
 * @Author petterp
 * @Date 2020/7/8-12:09 PM
 * @Email ShiyihuiCloud@163.com
 * @Function
 */
internal class StringAdapter : TypeAdapter<String?>() {
    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: String?) {
        if (value == null) {
            out.nullValue()
            return
        }
        out.value(value)
    }

    @Throws(IOException::class)
    override fun read(`in`: JsonReader): String {
        if (`in`.peek() === JsonToken.NULL) {
            `in`.nextNull()
            return ""
        }
        return `in`.nextString()
    }
}
