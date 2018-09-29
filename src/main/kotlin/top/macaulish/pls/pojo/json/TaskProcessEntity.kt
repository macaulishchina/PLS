package top.macaulish.pls.pojo.json

import com.google.gson.annotations.SerializedName

class TaskProcessEntity {
    @SerializedName(value = "taskGuid", alternate = ["guid", "task", "taskguid"])
    var taskGuid: String = ""
    @SerializedName(value = "taskState", alternate = ["taskstate", "state"])
    var taskState: String = ""
    var progress: String = ""
}