package com.cloudx.core.download


/**
 * Created by Petterp
 * on 2020-01-17
 * Function: 文件信息类 todo
 */
class DowloadInfo(private var mTotalLength: Long) {
    var mDownloadLength: Long = 0  //已下载字节
    private var scaleInt = 0
    //暂时需要以下材料


    fun getFileInt(): Int {
        val size = ((mDownloadLength.toDouble() / mTotalLength) * 100).toInt()
        scaleInt = size
        return scaleInt
    }

}