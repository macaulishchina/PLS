package top.macaulish.pls.service

import top.macaulish.pls.pojo.db.TaskEntity
import top.macaulish.pls.pojo.ice.*
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.io.OutputStream

interface _taskService : _iceInterface {

    fun isLocal(): Boolean

    fun createTask(task: TaskEntity): Boolean

    fun startTask(taskGuid: String): Boolean

    fun pauseTask(taskGuid: String): Boolean

    fun resumeTask(taskGuid: String): Boolean

    fun stopTask(taskGuid: String): Boolean

    fun deleteTask(taskGuid: String): Boolean

    fun queryTaskInfo(taskGuid: String): TaskInfo?

    fun queryTaskProcess(taskGuid: String): TaskProcessInfo?

    fun getUploadDir(taskGuid: String): PathInfo?

    fun getDownloadDir(taskGuid: String): PathInfo?

    fun getFtpInfo(): SFTPInfo?

    fun uploadFile(inputStream: InputStream, taskGuid: String, saveName: String): Boolean

    fun downloadResult(taskGuid: String): ByteArray?
}