package com.cloudx.core.factory

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import okio.IOException
import java.math.BigDecimal


/**
 * @Author petterp
 * @Date 2020/7/11-6:38 AM
 * @Email ShiyihuiCloud@163.com
 * @Function
 */
//自定义String适配器
internal val STRING: TypeAdapter<*>? = object : TypeAdapter<Any?>() {
    @Throws(IOException::class)
    override fun write(jsonWriter: JsonWriter, o: Any?) {
        if (o == null) {
            // 在这里处理null改为空字符串
            jsonWriter.value("")
            return
        }
        jsonWriter.value(o.toString())
    }

    @Throws(IOException::class)
    override fun read(reader: JsonReader): String? {
        if (reader.peek() === JsonToken.NULL) {
            reader.nextNull()
            return ""
        }
        return reader.nextString()
    }
}

//自定义Integer适配器
internal val INTEGER: TypeAdapter<*> = object : TypeAdapter<Any?>() {
    @Throws(IOException::class)
    override fun write(jsonWriter: JsonWriter, o: Any?) {
        if (o == null) {
            // 在这里处理null改为0
            jsonWriter.value(0)
            return
        }
        jsonWriter.value(Integer.valueOf(o.toString()))
    }

    @Throws(IOException::class)
    override fun read(reader: JsonReader): Int? {
        if (reader.peek() === JsonToken.NULL) {
            reader.nextNull()
            return 0
        }
        return reader.nextInt()
    }
}


//自定义BigDecimal适配器
internal val LONGIMAL: TypeAdapter<*> = object : TypeAdapter<Any?>() {
    @Throws(IOException::class)
    override fun write(jsonWriter: JsonWriter, o: Any?) {
        if (o == null) {
            // 在这里处理null改为0
            jsonWriter.value(BigDecimal("0"))
            return
        }
        jsonWriter.value(BigDecimal(o.toString()))
    }

    @Throws(IOException::class)
    override fun read(reader: JsonReader): BigDecimal? {
        if (reader.peek() === JsonToken.NULL) {
            reader.nextNull()
            return BigDecimal("0")
        }
        return BigDecimal(reader.nextString())
    }
}
