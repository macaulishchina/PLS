package top.macaulish.pls.service

import org.springframework.web.multipart.MultipartFile
import top.macaulish.pls.pojo.json.FTPEntity
import top.macaulish.pls.pojo.db.TaskEntity
import top.macaulish.pls.pojo.json.TaskInfoEntity
import top.macaulish.pls.pojo.json.TaskProcessEntity
import java.io.File

interface _taskService : _iceInterface {

    fun isLocal(): Boolean

    fun createTask(task: TaskEntity): Boolean

    fun startTask(taskGuid: String): Boolean

    fun pauseTask(taskGuid: String): Boolean

    fun resumeTask(taskGuid: String): Boolean

    fun stopTask(taskGuid: String): Boolean

    fun deleteTask(taskGuid: String): Boolean

    fun queryTaskInfo(taskGuid: String): TaskInfoEntity?

    fun queryTaskProcess(taskGuid: String): TaskProcessEntity?

    fun getUploadDir(taskGuid: String): String?

    fun getDownloadDir(taskGuid: String): String?

    fun getFtpInfo(): FTPEntity?

    fun uploadFile(file: MultipartFile, taskGuid: String): Boolean

    fun downloadFile(file: File, taskGuid: String): Boolean

}