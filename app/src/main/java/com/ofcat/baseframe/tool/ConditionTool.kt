package com.ofcat.baseframe.tool

import org.json.JSONObject
import java.lang.Exception

object ConditionTool {

    fun isJsonStr(str: String): Boolean {
        return try {
            val json = JSONObject(str)
            true
        } catch (e: Exception) {
            false

        }
    }
}