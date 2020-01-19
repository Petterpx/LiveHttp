@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.cloudx.core.download

import android.annotation.SuppressLint
import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.cloudx.core.LiveConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.*
import java.lang.Exception


/**
 * Created by Petterp
 * on 2020-01-17
 * Function: 文件操纵类
 * Android P 测试通过
 * Android 10 测试通过
 */
class DownloadInfo(private var mTotalLength: Long) {
    var mDownloadLength: Long = 0
    private var scaleInt = 0


    fun getFileInt(): Int {
        val size = ((mDownloadLength.toDouble() / mTotalLength) * 100).toInt()
        scaleInt = size
        return scaleInt
    }

}

@ExperimentalCoroutinesApi
suspend inline fun ResponseBody.overSchedule(
    fileDow: FileInfoKtx,
    crossinline obj: ((Uri) -> Unit) = {}
): Flow<Int> {
    return flow<Int> {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            val saveQ = saveQ(
                this@overSchedule,
                fileDow,
                DownloadInfo(this@overSchedule.contentLength()),
                this
            )
            withContext(Dispatchers.Main) {
                obj(saveQ)
            }
        } else {
            val saveFile = saveFile(
                this@overSchedule,
                fileDow,
                DownloadInfo(this@overSchedule.contentLength()),
                this
            )
            withContext(Dispatchers.Main) {
                obj(saveFile)
            }
        }
    }.flowOn(Dispatchers.IO)
}


suspend inline fun ResponseBody.over(fileDow: FileInfoKtx): Uri {
    return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
        saveQ(this, fileDow)
    } else {
        saveFile(this, fileDow)
    }
}


@SuppressLint("NewApi")
suspend inline fun saveQ(
    body: ResponseBody,
    fileDow: FileInfoKtx,
    fileInfo: DownloadInfo? = null,
    flow: FlowCollector<Int>? = null
): Uri {
    with(fileDow) {
        val values = ContentValues()
        //显示名称
        values.put(MediaStore.Downloads.DISPLAY_NAME, fileName)
        //存储文件的类型
//    values.put(MediaStore.Downloads.MIME_TYPE, mimeType)
        //私有文件的木库
        values.put(MediaStore.Downloads.RELATIVE_PATH, "Download/$path/")
        //生成一个Uri
        val external = MediaStore.Downloads.EXTERNAL_CONTENT_URI
        val resolver = LiveConfig.config.mContext.contentResolver
        val insertUri = resolver.insert(external, values)
        resolver.openOutputStream(insertUri!!)?.let {
            file(it, body.byteStream(), fileInfo, flow)
        }
        return insertUri
    }
}


suspend inline fun saveFile(
    body: ResponseBody,
    fileDow: FileInfoKtx,
    fileInfo: DownloadInfo? = null,
    flow: FlowCollector<Int>? = null
): Uri {
    with(fileDow) {
        val f =
            File(Environment.getExternalStorageDirectory().path + "/" + path + "/")
        if (!f.exists()) {
            f.mkdir()
        }
        val file = File(f.parent + "/" + path + "/" + fileName)
        file(FileOutputStream(file), body.byteStream(), fileInfo, flow)
        return Uri.fromFile(file)
    }
}

suspend inline fun file(
    os: OutputStream,
    ios: InputStream,
    fileInfo: DownloadInfo?,
    flow: FlowCollector<Int>?
) {
    //下载进度
    var downloadSiz = 0L
    try {
        var read: Int
        var sizeCopy = 0
        val buffer = ByteArray(8192)
        while (ios.read(buffer).apply { read = this } > 0) {
            os.write(buffer, 0, read)
            fileInfo?.let {
                downloadSiz += read
                fileInfo.mDownloadLength = downloadSiz
                val siz = fileInfo.getFileInt()
                if (siz > sizeCopy) {
                    flow?.emit(siz)
                    sizeCopy = siz
                }
            }
        }
    } catch (e: Exception) {
        Log.e("liveHttp", e.message.toString())
    } finally {
        os.close()
        ios.close()
    }
}