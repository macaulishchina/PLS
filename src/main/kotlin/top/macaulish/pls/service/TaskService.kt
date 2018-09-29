package top.macaulish.pls.service

import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import top.macaulish.pls.dao.TaskDao
import top.macaulish.pls.ice.client.TaskICEClient
import top.macaulish.pls.ice.client.TaskPrx
import top.macaulish.pls.kits.JsonConverter
import top.macaulish.pls.pojo.json.FTPEntity
import top.macaulish.pls.pojo.db.TaskEntity
import top.macaulish.pls.pojo.json.TaskInfoEntity
import top.macaulish.pls.pojo.json.TaskProcessEntity
import java.io.File
import java.sql.Timestamp
import java.util.*

@Service
class TaskService : _taskService {

    private lateinit var taskServer: TaskPrx

    private val log = Logger.getLogger(TaskService::class.java)

    @Value("#{ice.ice_server_if_local}")
    private var localHost: String = "true"

    @Autowired
    private lateinit var taskClient: TaskICEClient
    @Autowired
    private lateinit var modelService: ModelService
    @Autowired
    private lateinit var taskDao: TaskDao

    init {
        taskServer = taskClient.getTaskPrx()
    }

    override fun isLocal(): Boolean {
        return localHost.toLowerCase() == "true"
    }

    override fun createTask(task: TaskEntity): Boolean {
        try {
            if (task.modelGuid == null || task.userGuid == null || task.taskName == null || task.taskType == null) throw Exception("任务创建缺少必要的信息！")
            val taskServer = taskClient.getTaskPrx()
            task.guid = UUID.randomUUID().toString()
            //服务端尝试创建任务
            val actionBack = JsonConverter.fromActionBack(taskServer.create(task.modelGuid, task.guid, task.taskType))
            if (!actionBack.isSuccessBack()) throw Exception("服务端创建任务失败！${actionBack.reason}")
            //else:服务端任务创建成功
            task.createTime = Timestamp(System.currentTimeMillis())
            task.state = "ready"
            task.modelName = modelService.queryModel(task.modelGuid)?.name ?: "unknown model name"
            task.savePath = getUploadDir(task.guid)
            task.saveHost = getFtpInfo()?.host ?: "Not available"
            task.taskSize = 0
            task.taskNumber = 0
            //保存任务到数据库
            taskDao.save(task)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            log.error("创建任务失败！", e)
            return false
        }
    }

    override fun startTask(taskGuid: String): Boolean {
        return try {
            //检查数据库中是否存在这条任务记录
            val task = taskDao.queryFirst(taskGuid) ?: throw Exception("数据库中不存在此任务！")
            //服务端尝试启动任务
            val actionBack = JsonConverter.fromActionBack(taskServer.start(taskGuid))
            if (!actionBack.isSuccessBack()) throw Exception("服务端启动任务失败！${actionBack.reason}")
            //else:任务启动成功
            task.state = "handling"
            taskDao.save(task)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            log.error("启动任务失败！", e)
            false
        }
    }

    override fun pauseTask(taskGuid: String): Boolean {
        return try {
            //检查数据库中是否存在这条任务记录
            val task = taskDao.queryFirst(taskGuid) ?: throw Exception("数据库中不存在此任务！")
            //服务端尝试暂停任务
            val actionBack = JsonConverter.fromActionBack(taskServer.pause(taskGuid))
            if (!actionBack.isSuccessBack()) throw Exception("服务端暂停任务失败！${actionBack.reason}")
            //else:任务暂停成功
            task.state = "pause"
            taskDao.save(task)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            log.error("暂停任务失败！", e)
            false
        }
    }

    override fun resumeTask(taskGuid: String): Boolean {
        return try {
            //检查数据库中是否存在这条任务记录
            val task = taskDao.queryFirst(taskGuid) ?: throw Exception("数据库中不存在此任务！")
            //服务端尝试继续任务
            val actionBack = JsonConverter.fromActionBack(taskServer.resume(taskGuid))
            if (!actionBack.isSuccessBack()) throw Exception("服务端继续任务失败！${actionBack.reason}")
            //else:任务继续成功
            task.state = "handling"
            taskDao.save(task)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            log.error("恢复任务失败！", e)
            false
        }
    }

    override fun stopTask(taskGuid: String): Boolean {
        return try {
            //检查数据库中是否存在这条任务记录
            val task = taskDao.queryFirst(taskGuid) ?: throw Exception("数据库中不存在此任务！")
            //服务端尝试终止任务
            val actionBack = JsonConverter.fromActionBack(taskServer.stop(taskGuid))
            if (!actionBack.isSuccessBack()) throw Exception("服务端终止任务失败！${actionBack.reason}")
            //else:任务终止成功
            task.state = "stop"
            taskDao.save(task)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            log.error("终止任务失败！", e)
            false
        }
    }

    override fun deleteTask(taskGuid: String): Boolean {
        return try {
            //检查数据库中是否存在这条任务记录
            val task = taskDao.queryFirst(taskGuid) ?: throw Exception("数据库中不存在此任务！")
            //服务端尝试删除任务
            val actionBack = JsonConverter.fromActionBack(taskServer.stop(taskGuid))
            if (!actionBack.isSuccessBack()) throw Exception("服务端删除任务失败！${actionBack.reason}")
            //else:任务删除成功
            task.state = "delete"
            taskDao.save(task)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            log.error("删除任务失败！", e)
            false
        }
    }

    override fun queryTaskInfo(taskGuid: String): TaskInfoEntity? {
        return try {
            //查询服务端任务信息
            JsonConverter.fromTaskInfo(taskServer.query(taskGuid))
        } catch (e: Exception) {
            e.printStackTrace()
            log.error("查询任务失败！", e)
            null
        }
    }

    override fun queryTaskProcess(taskGuid: String): TaskProcessEntity? {
        return try {
            //查询服务端任务进程信息
            JsonConverter.fromTaskProgress(taskServer.getProgress(taskGuid))
        } catch (e: Exception) {
            e.printStackTrace()
            log.error("查询任务进程失败！", e)
            null
        }
    }

    override fun getUploadDir(taskGuid: String): String? {
        return try {
            //查询服务端任务上传目录
            JsonConverter.fromPath(taskServer.getUploadDir(taskGuid)).dir
        } catch (e: Exception) {
            e.printStackTrace()
            log.error("查询任务上传目录失败！", e)
            null
        }
    }

    override fun getDownloadDir(taskGuid: String): String? {
        return try {
            //查询服务端任务下载目录
            JsonConverter.fromPath(taskServer.getDownloadDir(taskGuid)).dir
        } catch (e: Exception) {
            e.printStackTrace()
            log.error("查询任务下载目录失败！", e)
            null
        }
    }

    override fun getFtpInfo(): FTPEntity? {
        return try {
            //查询服务端ftp连接信息
            JsonConverter.fromFTPInfo(taskServer.ftpInfo)
        } catch (e: Exception) {
            e.printStackTrace()
            log.error("获得ftp连接信息失败！", e)
            null
        }
    }

    override fun uploadFile(file: MultipartFile, taskGuid: String): Boolean {
        TODO("not implemented")
    }

    override fun downloadFile(file: File, taskGuid: String): Boolean {
        TODO("not implemented")
    }

}