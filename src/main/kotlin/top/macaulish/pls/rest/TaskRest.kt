package top.macaulish.pls.rest

import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import top.macaulish.pls.dao.TaskDao
import top.macaulish.pls.dao.UserDao
import top.macaulish.pls.dao._userDao
import top.macaulish.pls.kits.JsonResponse as jr
import top.macaulish.pls.pojo.db.TaskEntity
import top.macaulish.pls.service.TaskService

/**
 *@author huyidong
 *@date 2018/9/25
 */
@RestController
@RequestMapping(path = ["/task"], produces = ["application/json;charset=UTF-8"])
class TaskRest {

    private val log = Logger.getLogger(TaskRest::class.java)

    @Autowired
    private lateinit var taskService: TaskService
    @Autowired
    private lateinit var taskDao: TaskDao
    @Autowired
    private lateinit var userDao: UserDao


    @GetMapping(path = ["/{username}/query"])
    fun queryAll(@PathVariable username: String, @RequestParam(value = "update", required = false) update: Boolean = false): String {
        return try {
            val tasks = HashMap<String, List<TaskEntity>>()
            //查询所有公共任务
            var ex = TaskEntity()
            ex.publish = 1
            val publishTasks = taskDao.queryByExample(ex)

            val privateTasks: List<TaskEntity>
            val user = userDao.queryFirstByUsername(username)
            //用户名检查
            if (user == null) {
                privateTasks = emptyList()
            } else {
                //查询所有私人任务
                ex = TaskEntity()
                ex.userGuid = user.guid
                privateTasks = taskDao.queryByExample(ex)
            }
            //从模型服务端更新任务状态
            if (update) {
                val publishTasksNew = ArrayList<TaskEntity>()
                val privateTasksNew = ArrayList<TaskEntity>()
                for (task in publishTasks) {
                    taskService.updateTaskInfo(task.guid).let { if (it != null) publishTasksNew.add(it) }
                }
                for (task in privateTasks) {
                    taskService.updateTaskInfo(task.guid).let { if (it != null) privateTasksNew.add(it) }
                }
                tasks["publish"] = publishTasksNew
                tasks["private"] = privateTasksNew
                //以数量判断是否成功从模型服务端更新
                if (publishTasks.size + privateTasks.size - publishTasksNew.size - privateTasksNew.size > 0) {
                    jr.fail("update data failed")
                } else {
                    jr.success(tasks)
                }
            } else {
                tasks["publish"] = publishTasks
                tasks["private"] = privateTasks
                jr.success(tasks)
            }
        } catch (e: Exception) {
            log.error("fail to get all the task", e)
            jr.fail("unknown reason")
        }
    }

    @GetMapping(path = ["/{username}/query/{taskGuid}"])
    fun queryOne(@PathVariable username: String, @PathVariable taskGuid: String): String {

        return try {
            val user = userDao.queryFirstByUsername(username)
            val task = taskDao.queryFirst(taskGuid)
            val taskInfo = taskService.queryTaskInfo(taskGuid)
            if (task == null) {
                jr.fail("the task doesn't exist")
            } else if (taskInfo == null) {
                jr.fail("fail to update task info ")
            } else if (user == null && task.publish == 1) {
                jr.success(taskInfo)
            } else if (user != null && user.guid == task.userGuid) {
                jr.success(taskInfo)
            } else {
                jr.fail("permission denied")
            }
        } catch (e: Exception) {
            log.error("fail to query the task info", e)
            jr.fail("unknown reason")
        }
    }

}