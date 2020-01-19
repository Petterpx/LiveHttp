package com.cloudx.core.download

import com.cloudx.core.LiveConfig

/**
 * Created by Petterp
 * on 2020-01-19
 * Function:
 */

/**
 * 具体的数据类
 */
data class FileInfoKtx(
    /**
     * 文件名,需要后缀
     */
    var fileName: String,

    /**
     * 文件保存路径
     */
    var path: String = LiveConfig.config.downloadName
    )
