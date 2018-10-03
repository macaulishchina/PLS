package top.macaulish.pls.kits

import com.google.gson.Gson

object JsonResponse {

    fun success(): String {
        return JsonSimpleResult("success").toJsonString()
    }

    fun fail(): String {
        return JsonSimpleResult("fail").toJsonString()
    }

    fun success(result: Any): String {
        return JsonResult("success", result).toJsonString()
    }

    fun fail(reason: Any): String {
        return JsonResult("fail", reason).toJsonString()
    }

    private class JsonSimpleResult(val state: String) {
        fun toJsonString(): String {
            return Gson().toJson(this)
        }
    }

    private class JsonResult(val state: String, val data: Any) {
        fun toJsonString():String{
            return Gson().toJson(this)
        }

    }

}