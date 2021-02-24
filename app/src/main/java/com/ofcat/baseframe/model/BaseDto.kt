package com.ofcat.baseframe.model

import com.google.gson.annotations.SerializedName

/**
 * 通用資料格式
 */
open class BaseDto<T> {

    @SerializedName("code")
    var code: String? = null

    @SerializedName("message")
    var message: String? = null

    @SerializedName("data")
    var data: T? = null

    var apiName: String = ""

}