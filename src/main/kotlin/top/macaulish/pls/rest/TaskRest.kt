package top.macaulish.pls.rest

import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import top.macaulish.pls.dao.TaskDao
import top.macaulish.pls.dao.UserDao
import top.macaulish.pls.dao._userDao
import top.macaulish.pls.kits.JsonResponse as jr
import top.macaulish.pls.pojo.db.TaskEntity
import top.macaulish.pls.service.TaskService
import top.macaulish.pls.service.UserService

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
    @Autowired
    private lateinit var userService: UserService


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
                //特殊处理：以数量判断是否成功从模型服务端更新
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
            jr.fail("internal error")
        }
    }

    @GetMapping(path = ["/{username}/query/{taskGuid}"])
    fun queryOne(@PathVariable username: String, @PathVariable taskGuid: String): String {
        return try {
            val user = userDao.queryFirstByUsername(username)
            val task = taskDao.queryFirst(taskGuid)
            if (task == null) { //任务不存在
                jr.fail("the task doesn't exist")
            } else if (task.publish == 1) {    //查询公共任务
                taskService.queryTaskInfo(taskGuid).let { if (it == null) jr.fail("fail to update the task") else jr.success(it) }
            } else if (user != null && task.userGuid == user.guid) { //查询私有任务
                taskService.queryTaskInfo(taskGuid).let { if (it == null) jr.fail("fail to update the task") else jr.success(it) }
            } else {
                jr.fail("permission denied")
            }
        } catch (e: Exception) {
            log.error("fail to query the task info", e)
            jr.fail("internal error")
        }
    }

    @GetMapping(path = ["/{username}/query/process/{taskGuid}"])
    fun queryProcess(@PathVariable username: String, @PathVariable taskGuid: String): String {
        return try {
            val user = userDao.queryFirstByUsername(username)
            val task = taskDao.queryFirst(taskGuid)
            if (task == null) { //任务不存在
                jr.fail("the task doesn't exist")
            } else if (task.publish == 1) {    //查询公共任务
                taskService.queryTaskProcess(taskGuid).let { if (it == null) jr.fail("fail to update the task") else jr.success(it) }
            } else if (user != null && task.userGuid == user.guid) { //查询私有任务
                taskService.queryTaskInfo(taskGuid).let { if (it == null) jr.fail("fail to update the task") else jr.success(it) }
            } else {
                jr.fail("permission denied")
            }
        } catch (e: Exception) {
            log.error("fail to query the task process", e)
            jr.fail("internal error")
        }
    }

    @PostMapping(path = ["/{username}/create"])
    fun createTask(@PathVariable username: String, @RequestParam taskName: String, @RequestParam modelGuid: String, @RequestParam sourceType: String, @RequestParam resultType: String): String {
        return try {
            val user = userDao.queryFirstByUsername(username)
            if (user == null || !userService.isLegalUser(user)) {
                jr.fail("permission denied")
            } else {
                val task = TaskEntity()
                task.userGuid = user.guid
                task.taskName = taskName
                task.modelGuid = modelGuid
                task.sourceType = sourceType
                task.resultType = resultType
                task.state = "new"
                taskService.createTask(task).let { if (it) jr.success(task) else jr.fail("fail to create task") }
            }
        } catch (e: Exception) {
            log.error("fail to create task", e)
            jr.fail("internal error")
        }
    }

    @PostMapping(path = ["/{username}/start/{taskGuid}"])
    fun startTask(@PathVariable username: String, @PathVariable taskGuid: String): String {
        return try {
            val user = userDao.queryFirstByUsername(username)
            val task = taskDao.queryFirst(taskGuid)
            if (task == null) {
                jr.fail("the task doesn't exist")
            } else if (user == null || !userService.isLegalUser(user) || user.guid != task.userGuid) {
                jr.fail("permission denied")
            } else {
                taskService.startTask(taskGuid).let { if (it) jr.success() else jr.fail() }
            }
        } catch (e: Exception) {
            log.error("fail to start task", e)
            jr.fail("internal error")
        }
    }

    @PostMapping(path = ["/{username}/pause/{taskGuid}"])
    fun pauseTask(@PathVariable username: String, @PathVariable taskGuid: String): String {
        return try {
            val user = userDao.queryFirstByUsername(username)
            val task = taskDao.queryFirst(taskGuid)
            if (task == null) {
                jr.fail("the task doesn't exist")
            } else if (user == null || !userService.isLegalUser(user) || user.guid != task.userGuid) {
                jr.fail("permission denied")
            } else {
                taskService.pauseTask(taskGuid).let { if (it) jr.success() else jr.fail() }
            }
        } catch (e: Exception) {
            log.error("fail to pause task", e)
            jr.fail("internal error")
        }
    }

    @PostMapping(path = ["/{username}/resume/{taskGuid}"])
    fun resumeTask(@PathVariable username: String, @PathVariable taskGuid: String): String {
        return try {
            val user = userDao.queryFirstByUsername(username)
            val task = taskDao.queryFirst(taskGuid)
            if (task == null) {
                jr.fail("the task doesn't exist")
            } else if (user == null || !userService.isLegalUser(user) || user.guid != task.userGuid) {
                jr.fail("permission denied")
            } else {
                taskService.resumeTask(taskGuid).let { if (it) jr.success() else jr.fail() }
            }
        } catch (e: Exception) {
            log.error("fail to resume task", e)
            jr.fail("internal error")
        }
    }

    @PostMapping(path = ["/{username}/stop/{taskGuid}"])
    fun stopTask(@PathVariable username: String, @PathVariable taskGuid: String): String {
        return try {
            val user = userDao.queryFirstByUsername(username)
            val task = taskDao.queryFirst(taskGuid)
            if (task == null) {
                jr.fail("the task doesn't exist")
            } else if (user == null || !userService.isLegalUser(user) || user.guid != task.userGuid) {
                jr.fail("permission denied")
            } else {
                taskService.startTask(taskGuid).let { if (it) jr.success() else jr.fail() }
            }
        } catch (e: Exception) {
            log.error("fail to stop task", e)
            jr.fail("internal error")
        }
    }

    @PostMapping(path = ["/{username}/delete/{taskGuid}"])
    fun deleteTask(@PathVariable username: String, @PathVariable taskGuid: String): String {
        return try {
            val user = userDao.queryFirstByUsername(username)
            val task = taskDao.queryFirst(taskGuid)
            if (task == null) {
                jr.fail("the task doesn't exist")
            } else if (user == null || !userService.isLegalUser(user) || user.guid != task.userGuid) {
                jr.fail("permission denied")
            } else {
                taskService.deleteTask(taskGuid).let { if (it) jr.success() else jr.fail() }
            }
        } catch (e: Exception) {
            log.error("fail to delete task", e)
            jr.fail("internal error")
        }
    }

    @PostMapping(path = ["/{username}/upload/{taskGuid}"], consumes = ["multipart/form-data"])
    fun uploadFile(@PathVariable username: String, @PathVariable taskGuid: String, @RequestParam("file") file: MultipartFile): String {
        return try {
            val user = userDao.queryFirstByUsername(username)
            val task = taskDao.queryFirst(taskGuid)
            if (task == null) {
                jr.fail("the task doesn't exist")
            } else if (user == null || !userService.isLegalUser(user) || user.guid != task.userGuid) {
                jr.fail("permission denied")
            } else {
                taskService.uploadFile(file.inputStream, taskGuid, file.originalFilename ?: file.name)
                        .let {
                            if (it) jr.success(file.originalFilename
                                    ?: file.name) else jr.fail("fail to upload file ${file.originalFilename
                                    ?: file.name}")
                        }
            }
        } catch (e: Exception) {
            log.error("fail to upload file", e)
            jr.fail("internal error")
        }
    }

    @GetMapping(path = ["/{username}/download/result/{taskGuid}"], produces = ["application/octet-stream"])
    fun downloadResult(@PathVariable username: String, @PathVariable taskGuid: String): ResponseEntity<ByteArray> {
        return try {
            val user = userDao.queryFirstByUsername(username)
            val task = taskDao.queryFirst(taskGuid)
            if (task == null) {
                ResponseEntity(HttpStatus.NO_CONTENT)
            } else if (user == null || !userService.isLegalUser(user) || (user.guid != task.userGuid && task.publish == 0)) {
                ResponseEntity(HttpStatus.FORBIDDEN)
            } else {
                val result = taskService.downloadResult(taskGuid)
                if (result == null) {
                    ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
                } else {
                    val headers = HttpHeaders()
                    headers.setContentDispositionFormData("attachment", "$taskGuid.txt")
                    headers.contentType = MediaType.APPLICATION_OCTET_STREAM
                    headers.setContentDispositionFormData("attachment", "${task.taskName}.zip")
                    ResponseEntity(result, headers, HttpStatus.CREATED)
                }
            }
        } catch (e: Exception) {
            log.error("fail to download task", e)
            ResponseEntity("internal error".toByteArray(), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping(path = ["/{username}/download/source/{taskGuid}"], produces = ["application/octet-stream"])
    fun downloadSource(): ResponseEntity<ByteArray> {
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

}