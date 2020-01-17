@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.cloudx.core.utils

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.telephony.mbms.FileInfo
import com.cloudx.core.download.DowloadInfo
import com.cloudx.core.download.FileLister
import com.cloudx.core.error.ErrorCodeKts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import java.io.*

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

inline fun requestBody(obj: () -> Map<String, String>): RequestBody =
    LiveConfig.config.mGson.toJson(obj()).toRequestBody(LiveConfig.config.mediaType)


/**
 * 多文件上传，文件类型应该框架自动推导，暂时todo,需手动传入
 */
data class FileBean(
    var file: File,
    var fileName: String = file.name,
    var fileType: String = "image/jpg"
)

inline fun fileBody(obj: () -> FileBean): MultipartBody.Part {
    with(obj()) {
        val requestBody: RequestBody = file.asRequestBody(fileType.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("file", fileName, requestBody)
    }
}

inline fun fileBodys(obj: () -> List<FileBean>): List<MultipartBody.Part> {
    val list = ArrayList<MultipartBody.Part>(3)
    obj().forEach {
        list += fileBody { it }
    }
    return list
}


data class FileDow(var fileName: String, var path: String, var fileType: String = "image/jpg")

suspend inline fun ResponseBody.obver(fileDow: FileDow): Flow<Int> {
    with(fileDow) {
        //        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
//            saveFileQ(
//                LiveConfig.config.mContext,
//                this@obver, path, fileName, fileType
//            )
//        } else {
        return saveFile(this@obver, path, fileName, DowloadInfo(this@obver.contentLength()))
//        }
    }

}


@SuppressLint("NewApi")
fun saveFileQ(
    context: Context,
    body: ResponseBody,
    path: String,
    name: String?,
    mimeType: String?
): Uri? {
    val values = ContentValues()
    //显示名称
    values.put(MediaStore.Downloads.DISPLAY_NAME, name)
    //存储文件的类型
    values.put(MediaStore.Downloads.MIME_TYPE, mimeType)
    //私有文件的木库
    values.put(MediaStore.Downloads.RELATIVE_PATH, "Download/$path/")
    //生成一个Uri
    val external = MediaStore.Downloads.EXTERNAL_CONTENT_URI
    val resolver = context.contentResolver
    val insertUri = resolver.insert(external, values)
    val `is`: InputStream
    var os: OutputStream? = null
    try { //输出流
        os = resolver.openOutputStream(insertUri!!)
        var read: Int
        `is` = body.byteStream() // 读入原文件
        val buffer = ByteArray(4096)
        while (`is`.read(buffer).apply { read = this } > 0) { //写入uri中
            os!!.write(buffer, 0, read)
        }
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        os?.close()
    }
    return insertUri
}


suspend inline fun saveFile(
    body: ResponseBody,
    path: String,
    fileName: String,
    fileInfo: DowloadInfo? = null
): Flow<Int> {
    return flow {
        val f =
            File(Environment.getExternalStorageDirectory().path + "/" + path + "/")
        if (!f.exists()) {
            f.mkdir()
        }
        val file = File(f.parent + "/" + path + "/" + fileName)
        val `is`: InputStream
        //下载进度
        var dowloadSize = 0L
        var os: OutputStream? = null
        try {
            var read: Int
            var sizecopy = 0
            os = FileOutputStream(file)
            `is` = body.byteStream() // 读入原文件
            val buffer = ByteArray(8192)
            while (`is`.read(buffer).apply { read = this } > 0) {
                os.write(buffer, 0, read)
                fileInfo?.let {
                    dowloadSize += read
                    fileInfo.mDownloadLength = dowloadSize
                    val siz = fileInfo.getFileInt()
                    if (siz > sizecopy) {
                        emit(siz)
                        sizecopy = siz
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (os != null) {
                try {
                    os.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

//    return Uri.fromFile(file)
}