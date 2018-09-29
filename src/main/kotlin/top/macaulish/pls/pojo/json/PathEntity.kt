package top.macaulish.pls.pojo.json

import com.google.gson.annotations.SerializedName

class PathEntity {
    @SerializedName(value = "taskGuid", alternate = ["task", "guid"])
    var taskGuid: String = ""
    @SerializedName(value = "dir", alternate = ["path"])
    var dir: String = ""
}