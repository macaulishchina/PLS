package top.macaulish.pls.pojo.json

import com.google.gson.annotations.SerializedName

class ActionResultEntity {
    @SerializedName(value = "guid", alternate = ["taskGuid", "modelGuid"])
    val guid: String = ""
    val action: String = ""
    val result: String = ""
    val reason: String = ""

    fun isSuccessBack(): Boolean {
        return result.trim().toLowerCase() == "success"
    }

    override fun toString(): String {
        return "{" +
                "\"guid\":\"" + guid + "\"," +
                "\"action\":\"" + action + "\"," +
                "\"result\":\"" + result + "\"," +
                "\"reason\":\"" + reason + "\"" +
                "}"
    }
}