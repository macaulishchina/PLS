package top.macaulish.pls.control

import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import top.macaulish.pls.dao.TaskDao
import top.macaulish.pls.kits.JsonResponse
import top.macaulish.pls.pojo.db.TaskEntity
import top.macaulish.pls.pojo.db.UserEntity
import top.macaulish.pls.service.TaskService
import java.util.*


@Controller
@RequestMapping(path = ["/{userGuid}/task"], produces = ["application/json;charset=UTF-8"])
class TaskController {

    private val log = Logger.getLogger(TaskController::class.java)

    @Autowired
    private lateinit var taskService:TaskService
    @Autowired
    private lateinit var taskDao:TaskDao

    @GetMapping(path = ["/all"])
    @ResponseBody
    fun getAllTasks():String{
        return try {
            val tasks = taskDao.queryByExample(TaskEntity())
            JsonResponse.success(tasks)
        }catch (e:Exception){
            log.error("fail to get all the task", e)
            JsonResponse.fail(e)
        }
    }

    @PostMapping(path = ["/create"])
    @ResponseBody
    fun createTask(@RequestBody task: TaskEntity): String {

        return if (taskService.createTask(task)) {
            JsonResponse.success("Created task ${task.taskName} successfully.")
        } else {
            JsonResponse.fail("Failed to create task ${task.taskName}.")
        }
    }

    @GetMapping(path = ["/query/{taskGuid}"])
    @ResponseBody
    fun queryTask(@PathVariable taskGuid: String) {

    }

}