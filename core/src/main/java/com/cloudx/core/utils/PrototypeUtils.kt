package com.cloudx.core.utils

import okhttp3.Response

/**
 * Created by Petterp
 * on 2020-01-17
 * Function: 深拷贝utils
 */

class PrototypeUtils(var responseBody: Response) : Cloneable {

    //实现克隆接口
    //重写克隆方法，默认浅克隆
    public override fun clone(): Any {
        val c = super.clone()
        if (c is PrototypeUtils) c.responseBody = responseBody
        return c
    }
}