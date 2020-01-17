package com.cloudx.core.interceptor

import okhttp3.Response
import okhttp3.ResponseBody

/**
 * Created by Petterp
 * on 2020-01-17
 * Function:
 */

class Prototype(var responseBody: Response) : Cloneable {

    //实现克隆接口
    //重写克隆方法，默认浅克隆
    public override fun clone(): Any {
        val c = super.clone()
        if (c is Prototype) c.responseBody = responseBody
        return c
    }
}