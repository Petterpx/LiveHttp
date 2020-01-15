package com.cloudx.core.error

import android.util.SparseArray
import androidx.core.util.contains
import androidx.core.util.putAll

/**
 * Created by Petterp
 * on 2020-01-14
 * Function: 服务器错误码处理
 */
object ErrorCodeKts {

    private val mSparseArray = SparseArray<CodeBean>()
    /**
     * put错误码
     */
    fun putCode(code: Int, codeBean: CodeBean): ErrorCodeKts {
        mSparseArray.put(code, codeBean)
        return this
    }

    fun putCodeAll(codeArray: SparseArray<CodeBean>) {
        mSparseArray.putAll(codeArray)
    }

    fun getCode(code: Int): CodeBean {
        if (code in mSparseArray) {
            return mSparseArray[code]
        }
        return CodeBean("未知错误")
    }
}
