package top.macaulish.pls.service

import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import top.macaulish.pls.dao.TaskDao
import top.macaulish.pls.ice.client.TaskICEClient
import top.macaulish.pls.ice.client.TaskPrx
import top.macaulish.pls.kits.FileKits
import top.macaulish.pls.kits.SFTPKits
import top.macaulish.pls.pojo.db.TaskEntity
import top.macaulish.pls.pojo.ice.*
import java.io.*
import java.net.InetAddress
import java.sql.Timestamp
import java.util.*

@Service
class TaskService : _taskService {

    private lateinit var taskServer: TaskPrx

    private val log = Logger.getLogger(TaskService::class.java)

    @Autowired
    private lateinit var taskClient: TaskICEClient
    @Autowired
    private lateinit var modelService: ModelService
    @Autowired
    private lateinit var taskDao: TaskDao

    private lateinit var sftpKits: SFTPKits

    init {
        taskServer = taskClient.getTaskPrx()
        if (!isLocal()) {
            val sftp = taskServer.ftpInfo
            sftpKits = SFTPKits(sftp.host, sftp.username, sftp.password, sftp.port)
            log.info("sftp:$sftpKits")
        }
    }

    override fun createTask(task: TaskEntity): Boolean {
        try {
            if (task.modelGuid == null || task.userGuid == null || task.taskName == null || task.taskType == null) throw Exception("任务创建缺少必要的信息！")
            val taskServer = taskClient.getTaskPrx()
            task.guid = UUID.randomUUID().toString()
            //服务端尝试创建任务
            val actionBack = taskServer.create(task.modelGuid, task.guid, task.taskType)
            if (!actionBack.isSuccessBack) throw Exception("服务端创建任务失败！${actionBack.reason}")
            //else:服务端任务创建成功
            task.createTime = Timestamp(System.currentTimeMillis())
            task.state = "ready"
            task.modelName = modelService.queryModel(task.modelGuid)?.name ?: "unknown model name"
            task.savePath = getUploadDir(task.guid)?.path ?: "Not available"
            task.saveHost = getFtpInfo()?.host ?: "Not available"
            task.taskSize = 0
            task.taskNumber = 0
            //保存任务到数据库
            taskDao.save(task)
            return true
        } catch (e: Exception) {
            log.error("创建任务失败！", e)
            return false
        }
    }

    override fun startTask(taskGuid: String): Boolean {
        return try {
            //检查数据库中是否存在这条任务记录
            val task = taskDao.queryFirst(taskGuid) ?: throw Exception("数据库中不存在此任务！")
            //服务端尝试启动任务
            val actionBack = taskServer.start(taskGuid)
            if (!actionBack.isSuccessBack) throw Exception("服务端启动任务失败！${actionBack.reason}")
            //else:任务启动成功
            task.state = "handling"
            taskDao.save(task)
            true
        } catch (e: Exception) {
            log.error("启动任务失败！", e)
            false
        }
    }

    override fun pauseTask(taskGuid: String): Boolean {
        return try {
            //检查数据库中是否存在这条任务记录
            val task = taskDao.queryFirst(taskGuid) ?: throw Exception("数据库中不存在此任务！")
            //服务端尝试暂停任务
            val actionBack = taskServer.pause(taskGuid)
            if (!actionBack.isSuccessBack) throw Exception("服务端暂停任务失败！${actionBack.reason}")
            //else:任务暂停成功
            task.state = "pause"
            taskDao.save(task)
            true
        } catch (e: Exception) {
            log.error("暂停任务失败！", e)
            false
        }
    }

    override fun resumeTask(taskGuid: String): Boolean {
        return try {
            //检查数据库中是否存在这条任务记录
            val task = taskDao.queryFirst(taskGuid) ?: throw Exception("数据库中不存在此任务！")
            //服务端尝试继续任务
            val actionBack = taskServer.resume(taskGuid)
            if (!actionBack.isSuccessBack) throw Exception("服务端继续任务失败！${actionBack.reason}")
            //else:任务继续成功
            task.state = "handling"
            taskDao.save(task)
            true
        } catch (e: Exception) {
            log.error("恢复任务失败！", e)
            false
        }
    }

    override fun stopTask(taskGuid: String): Boolean {
        return try {
            //检查数据库中是否存在这条任务记录
            val task = taskDao.queryFirst(taskGuid) ?: throw Exception("数据库中不存在此任务！")
            //服务端尝试终止任务
            val actionBack = taskServer.stop(taskGuid)
            if (!actionBack.isSuccessBack) throw Exception("服务端终止任务失败！${actionBack.reason}")
            //else:任务终止成功
            task.state = "stop"
            taskDao.save(task)
            true
        } catch (e: Exception) {
            log.error("终止任务失败！", e)
            false
        }
    }

    override fun deleteTask(taskGuid: String): Boolean {
        return try {
            //检查数据库中是否存在这条任务记录
            val task = taskDao.queryFirst(taskGuid) ?: throw Exception("数据库中不存在此任务！")
            //服务端尝试删除任务
            val actionBack = taskServer.stop(taskGuid)
            if (!actionBack.isSuccessBack) throw Exception("服务端删除任务失败！${actionBack.reason}")
            //else:任务删除成功
            task.state = "delete"
            taskDao.save(task)
            true
        } catch (e: Exception) {
            log.error("删除任务失败！", e)
            false
        }
    }

    override fun queryTaskInfo(taskGuid: String): TaskInfo? {
        return try {
            //检查数据库中是否存在这条任务记录
            val task = taskDao.queryFirst(taskGuid) ?: throw Exception("数据库中不存在此任务！")
            //查询服务端任务信息
            val taskInto = taskServer.query(taskGuid)
            task.state = taskInto.state
            task.modelGuid = taskInto.modelGuid
            task.modelName = taskInto.modelName
            taskDao.save(task)
            taskInto
        } catch (e: Exception) {
            log.error("查询任务失败！", e)
            null
        }
    }

    override fun queryTaskProcess(taskGuid: String): TaskProcessInfo? {
        return try {
            //检查数据库中是否存在这条任务记录
            val task = taskDao.queryFirst(taskGuid) ?: throw Exception("数据库中不存在此任务！")
            //查询服务端任务进程信息
            val processInfo = taskServer.getProgress(taskGuid)
            task.state = processInfo.taskState
            taskDao.save(task)
            processInfo
        } catch (e: Exception) {
            log.error("查询任务进程失败！", e)
            null
        }
    }

    override fun getUploadDir(taskGuid: String): PathInfo? {
        return try {
            //检查数据库中是否存在这条任务记录
            taskDao.queryFirst(taskGuid) ?: throw Exception("数据库中不存在此任务！")
            //查询服务端任务上传目录
            taskServer.getUploadDir(taskGuid)
        } catch (e: Exception) {
            log.error("查询任务上传目录失败！", e)
            null
        }
    }

    override fun getDownloadDir(taskGuid: String): PathInfo? {
        return try {
            //检查数据库中是否存在这条任务记录
            taskDao.queryFirst(taskGuid) ?: throw Exception("数据库中不存在此任务！")
            //查询服务端任务下载目录
            taskServer.getDownloadDir(taskGuid)
        } catch (e: Exception) {
            log.error("查询任务下载目录失败！", e)
            null
        }
    }

    override fun getFtpInfo(): SFTPInfo? {
        return try {
            //查询服务端ftp连接信息
            taskServer.ftpInfo
        } catch (e: Exception) {
            log.error("获得ftp连接信息失败！", e)
            null
        }
    }

    final override fun isLocal(): Boolean {
        return InetAddress.getLocalHost().hostAddress == taskServer.ftpInfo.host
    }

    override fun uploadFile(inputStream: InputStream, taskGuid: String, saveName: String): Boolean {
        return try {
            val uploadDir = taskServer.getUploadDir(taskGuid)
            if (isLocal()) {
                FileKits.saveFile(inputStream, uploadDir.path, saveName)
            } else {
                sftpKits.upload(uploadDir.path, saveName, inputStream)
            }
        } catch (e: Exception) {
            log.error("fail to upload file $saveName for task $taskGuid", e)
            false
        }
    }

    override fun downloadResult(taskGuid: String): ByteArray? {
        return try {
            val input: FileInputStream
            val resultPath = taskServer.getDownloadResultZip(taskGuid)
            if (isLocal()) {
                val result = File(resultPath)
                if (result.exists() && result.isFile) {
                    input = FileInputStream(result)
                    input.readBytes()
                } else {
                    log.debug("local file $resultPath doesn't exist")
                    null
                }
            } else {
                if (sftpKits.existAbsolutelyFile(resultPath)) {
                    val dir = FileKits.getParentPath(resultPath)
                    val file = FileKits.getFileName(resultPath)
                    sftpKits.downloadBytes(dir, file)
                } else {
                    log.debug("remote file $resultPath doesn't exist:$sftpKits")
                    null
                }
            }
        } catch (e: Exception) {
            log.error("fail to download file for task $taskGuid", e)
            null
        }
    }

}