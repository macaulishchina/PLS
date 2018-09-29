package top.macaulish.pls.kits

import com.google.gson.Gson

object JsonResponse {

    fun success(result:Any):String{
        return JsonResult("success",result).toJsonString()
    }

    fun fail(reason:Any):String{
        return JsonResult("fail",reason).toJsonString()
    }

    private class JsonResult(val state:String,var data:Any){
        fun toJsonString():String{
            return Gson().toJson(this)
        }
    }

}