package com.ofcat.baseframe.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ConfigDataDto : Serializable {

    /** apk更新說明 */
    @SerializedName("apk_des")
    var apkDes: String? = null

    /** 維護頁開關 */
    @SerializedName("maintain_switch")
    var maintainSwitch: String? = null


}