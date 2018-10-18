package top.macaulish.pls.service

import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import top.macaulish.pls.dao.TaskDao
import top.macaulish.pls.service.ice.client.TaskICEClient
import top.macaulish.pls.kits.FileKits
import top.macaulish.pls.kits.SFTPKits
import top.macaulish.pls.pojo.db.TaskEntity
import top.macaulish.pls.pojo.extend.FileInfo
import top.macaulish.pls.pojo.ice.*
import java.io.*
import java.net.InetAddress
import java.sql.Timestamp
import java.util.*

@Service
class TaskService : _TaskService {


    private val log = Logger.getLogger(TaskService::class.java)

    @Autowired
    private lateinit var taskClient: TaskICEClient
    @Autowired
    private lateinit var modelService: ModelService
    @Autowired
    private lateinit var taskDao: TaskDao

    override fun createTask(task: TaskEntity): Boolean {
        try {
            if (task.modelGuid == null || task.userGuid == null || task.taskName == null || task.sourceType == null ||
                    task.modelGuid.isBlank() || task.userGuid.isBlank() || task.taskName.isBlank() || task.sourceType.isBlank())
                throw Exception("任务创建缺少必要的信息！")
            val taskServer = taskClient.getTaskPrx()
            task.guid = UUID.randomUUID().toString()
            //服务端尝试创建任务
            val actionBack = taskServer.create(task.modelGuid, task.guid, task.sourceType, task.resultType)
            if (!actionBack.isSuccessBack) throw Exception("服务端创建任务失败！${actionBack.reason}")
            //else:服务端任务创建成功
            task.createTime = Timestamp(System.currentTimeMillis())
            task.state = "new"
            task.modelName = modelService.queryModel(task.modelGuid)?.name ?: "unknown model name"
            task.savePath = getUploadDir(task.guid)?.path ?: "not available"
            task.saveHost = getFtpInfo(task.guid)?.host ?: "not available"
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
            val actionBack = taskClient.getTaskPrx().start(taskGuid)
            if (!actionBack.isSuccessBack) throw Exception("服务端启动任务失败！${actionBack.reason}")
            //else:任务启动成功
            task.state = "handling"
            taskDao.saveOrUpdate(task)
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
            val actionBack = taskClient.getTaskPrx().pause(taskGuid)
            if (!actionBack.isSuccessBack) throw Exception("服务端暂停任务失败！${actionBack.reason}")
            //else:任务暂停成功
            task.state = "pause"
            taskDao.saveOrUpdate(task)
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
            val actionBack = taskClient.getTaskPrx().resume(taskGuid)
            if (!actionBack.isSuccessBack) throw Exception("服务端继续任务失败！${actionBack.reason}")
            //else:任务继续成功
            task.state = "handling"
            taskDao.saveOrUpdate(task)
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
            val actionBack = taskClient.getTaskPrx().stop(taskGuid)
            if (!actionBack.isSuccessBack) throw Exception("服务端终止任务失败！${actionBack.reason}")
            //else:任务终止成功
            task.state = "stop"
            taskDao.saveOrUpdate(task)
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
            val actionBack = taskClient.getTaskPrx().delete(taskGuid)
            if (!actionBack.isSuccessBack) throw Exception("服务端删除任务失败！${actionBack.reason}")
            //else:任务删除成功
            taskDao.delete(task)
            true
        } catch (e: Exception) {
            log.error("删除任务失败！", e)
            false
        }
    }

    override fun updateTaskInfo(taskGuid: String): TaskEntity? {
        return try {
            //检查数据库中是否存在这条任务记录
            val task = taskDao.queryFirst(taskGuid) ?: throw Exception("数据库中不存在此任务！")
            //查询服务端任务信息
            val taskInto = taskClient.getTaskPrx().query(taskGuid)
            if (taskInto.guid == taskGuid) {//任务存在，更新任务
                task.state = taskInto.state
                task.modelGuid = taskInto.modelGuid
                task.modelName = taskInto.modelName
            } else {
                task.state = "error"
            }
            taskDao.saveOrUpdate(task)
            task
        } catch (e: Exception) {
            log.error("更新任务失败！", e)
            null
        }
    }

    override fun queryTaskInfo(taskGuid: String): TaskInfo? {
        return try {
            //检查数据库中是否存在这条任务记录
            val task = taskDao.queryFirst(taskGuid) ?: throw Exception("数据库中不存在此任务！")
            //查询服务端任务信息
            val taskInfo = taskClient.getTaskPrx().query(taskGuid)
            if (taskInfo.guid == taskGuid) {
                task.state = taskInfo.state
                task.modelGuid = taskInfo.modelGuid
                task.modelName = taskInfo.modelName
                taskDao.saveOrUpdate(task)
                taskInfo
            } else {
                log.debug("模型端不存在此任务")
                null
            }
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
            val processInfo = taskClient.getTaskPrx().getProgress(taskGuid)
            task.state = processInfo.taskState
            taskDao.saveOrUpdate(task)
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
            taskClient.getTaskPrx().getUploadDir(taskGuid)
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
            taskClient.getTaskPrx().getDownloadDir(taskGuid)
        } catch (e: Exception) {
            log.error("查询任务下载目录失败！", e)
            null
        }
    }

    override fun getFtpInfo(taskGuid: String): SFTPInfo? {
        return try {
            //查询服务端ftp连接信息
            taskClient.getTaskPrx().getFTPInfo(taskGuid)
        } catch (e: Exception) {
            log.error("获得ftp连接信息失败！", e)
            null
        }
    }

    final override fun isLocal(taskGuid: String): Boolean {
        return InetAddress.getLocalHost().hostAddress == taskClient.getTaskPrx().getFTPInfo(taskGuid).host
    }

    override fun uploadFile(inputStream: InputStream, taskGuid: String, saveName: String): Boolean {
        var sftpKits: SFTPKits? = null
        return try {
            val taskPrx = taskClient.getTaskPrx()
            val uploadDir = taskPrx.getUploadDir(taskGuid)
            if (isLocal(taskGuid)) {
                FileKits.saveFile(inputStream, uploadDir.path, saveName)
            } else {
                val sftp = taskPrx.getFTPInfo(taskGuid)
                sftpKits = SFTPKits(sftp.host, sftp.username, sftp.password, sftp.port)
                sftpKits.login()
                sftpKits.upload(uploadDir.path, saveName, inputStream)
            }
        } catch (e: Exception) {
            log.error("fail to upload file $saveName for task $taskGuid", e)
            false
        } finally {
            sftpKits?.logout()
        }
    }

    override fun downloadResult(taskGuid: String): ByteArray? {
        var sftpKits: SFTPKits? = null
        var resultPath: String
        return try {
            val taskPrx = taskClient.getTaskPrx()
            val input: FileInputStream
            val pathInfo = taskPrx.getDownloadResultZip(taskGuid)
            if (taskGuid == pathInfo.taskGuid) {
                resultPath = taskPrx.getDownloadResultZip(taskGuid).path
            } else {
                throw Exception("result doesn't exist")
            }
            if (isLocal(taskGuid)) {
                val result = File(resultPath)
                if (result.exists() && result.isFile) {
                    input = FileInputStream(result)
                    input.readBytes()
                } else {
                    log.debug("local file $resultPath doesn't exist")
                    null
                }
            } else {
                val sftp = taskPrx.getFTPInfo(taskGuid)
                sftpKits = SFTPKits(sftp.host, sftp.username, sftp.password, sftp.port)
                sftpKits.login()
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
        } finally {
            try {
                sftpKits?.logout()
            } catch (e: Exception) {
                throw e
            }
        }
    }

    fun getUploadFiles(taskGuid: String): ArrayList<FileInfo> {
        var sftpKits: SFTPKits? = null
        val fileInfos = ArrayList<FileInfo>()
        return try {
            val dir = getUploadDir(taskGuid)
            val taskPrx = taskClient.getTaskPrx()
            val sftp = taskPrx.getFTPInfo(taskGuid)
            sftpKits = SFTPKits(sftp.host, sftp.username, sftp.password, sftp.port)
            sftpKits.login()
            val filesNames = sftpKits.lsFiles(dir?.path ?: throw Exception("upload files doesn't exist"))
            for (name in filesNames) {
                val fileInfo = FileInfo()
                fileInfo.name = name
                fileInfo.path = "$dir/$name"
                fileInfo.size = "unknown"
                fileInfos.add(fileInfo)
            }
            fileInfos
        } catch (e: Exception) {
            log.error(e)
            throw e
        } finally {
            try {
                sftpKits?.logout()
            } catch (e: Exception) {
                throw e
            }
        }
    }
}