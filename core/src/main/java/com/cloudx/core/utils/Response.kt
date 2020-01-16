package com.cloudx.core.utils

import com.cloudx.core.error.ErrorCodeKts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

/**
 * Created by Petterp
 * on 2020-01-14
 * Function:
 */
class LiveResponse<T>(val result: Result, val t: T)

class Result(var c: Int, var m: String)


inline fun <T> LiveResponse<T>.block(
    noinline blockError: ((Result) -> Unit)? = null,
    success: (T) -> Unit
) {
    if (result.c == 200) {
        success(t)
    } else {
        blockError?.let { it(result) } ?: ErrorCodeKts.getCode(result.c).obj.invoke()
    }
}

suspend inline fun <T> LiveResponse<T>.blockMain(
    noinline error: ((Result) -> Unit)? = null,
    noinline success: (T) -> Unit
) {
    withContext(Dispatchers.Main) {
        block(error, success)
    }
}


suspend inline fun <T> LiveResponse<T>.blockIO(
    noinline error: ((Result) -> Unit)? = null,
    noinline success: (T) -> Unit
) {
    withContext(Dispatchers.IO) {
        block(error, success)
    }
}



/**
 * requestBody
 */

fun requestBody(obj: () -> Map<String, Any>): RequestBody =
    LiveConfig.config.mGson.toJson(obj).toRequestBody(LiveConfig.config.mediaType)


/**
 * 多文件上传，文件类型应该框架自动推导，暂时todo,需手动传入
 */
data class FileBean(
    var file: File,
    var fileType: String = "image/jpg",
    var name: String = file.name
)

inline fun fileBody(obj: () -> FileBean): MultipartBody.Part {
    with(obj()) {
        val requestBody: RequestBody = file.asRequestBody(fileType.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(name, file.name, requestBody)
    }
}

inline fun fileBodys(obj: () -> List<FileBean>): List<MultipartBody.Part> {
    val list = ArrayList<MultipartBody.Part>(3)
    obj().forEach {
        list += fileBody { it }
    }
    return list
}


/**
 * 文件下载,适配Android 10,带进度返回
 */
fun downloadUtis(){

}
