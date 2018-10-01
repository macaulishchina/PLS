package top.macaulish.pls.pojo.json

import com.google.gson.annotations.SerializedName

class TaskInfoEntity {
    var guid: String = ""
    @SerializedName(value = "taskType", alternate = ["tasktype", "type"])
    var state: String = ""
    @SerializedName(value = "modelGuid", alternate = ["model"])
    var modelGuid: String = ""
    var modelName: String = ""
    @SerializedName(value = "totalSize", alternate = ["size"])
    var totalSize: String = ""
    @SerializedName(value = "fileNumber", alternate = ["number"])
    var fileNumber: String = ""
}