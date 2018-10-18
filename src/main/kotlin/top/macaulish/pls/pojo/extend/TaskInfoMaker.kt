package top.macaulish.pls.pojo.extend

import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import top.macaulish.pls.dao.UserDao
import top.macaulish.pls.pojo.db.TaskEntity

/**
 *@author huyidong
 *@date 2018/10/8
 */
@Component
class TaskInfoMaker {

    private val log = Logger.getLogger(TaskInfoMaker::class.java)

    @Autowired
    private lateinit var userDao: UserDao

    private fun extendProperties(info: TaskInfo, task: TaskEntity) {
        try {
            info.creatorName = userDao.queryFirst(task.userGuid)?.username ?: "unknown"
        } catch (e: Exception) {
            log.error("fail to extend properties", e)
        }
    }

    fun extend(task: TaskEntity): TaskInfo {
        val info = TaskInfo()
        info.id = task.id
        info.guid = task.guid
        info.taskName = task.taskName
        info.sourceType = task.sourceType
        info.resultType = task.resultType
        info.publish = task.publish
        info.state = task.state
        info.modelGuid = task.modelGuid
        info.modelName = task.modelName
        info.userGuid = task.userGuid
        info.createTime = task.createTime
        info.savePath = task.savePath
        info.saveHost = task.saveHost
        info.taskNumber = task.taskNumber
        info.taskSize = task.taskSize
        extendProperties(info, task)
        return info
    }
}