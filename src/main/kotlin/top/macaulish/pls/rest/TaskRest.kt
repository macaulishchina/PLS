package top.macaulish.pls.control

import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import top.macaulish.pls.dao.TaskDao
import top.macaulish.pls.kits.JsonResponse
import top.macaulish.pls.pojo.db.TaskEntity
import top.macaulish.pls.service.TaskService

/**
 *@author huyidong
 *@date 2018/9/25
 */
@RestController
@RequestMapping(path = ["/{userGuid}/task"], produces = ["application/json;charset=UTF-8"])
class TaskRest {

    private val log = Logger.getLogger(TaskRest::class.java)

    @Autowired
    private lateinit var taskService: TaskService
    @Autowired
    private lateinit var taskDao: TaskDao

    @GetMapping(path = ["/all"])
    fun getAllTasks(): String {
        return try {
            val tasks = taskDao.queryByExample(TaskEntity())
            JsonResponse.success(tasks)
        } catch (e: Exception) {
            log.error("fail to get all the task", e)
            JsonResponse.fail(e)
        }
    }

    @PostMapping(path = ["/create"])
    fun createTask(@RequestBody task: TaskEntity): String {

        return if (taskService.createTask(task)) {
            JsonResponse.success("Created task ${task.taskName} successfully.")
        } else {
            JsonResponse.fail("Failed to create task ${task.taskName}.")
        }
    }

    @GetMapping(path = ["/query/{taskGuid}"])
    fun queryTask(@PathVariable taskGuid: String) {

    }

}