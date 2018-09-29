package top.macaulish.pls.control

import org.apache.log4j.Logger
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import top.macaulish.pls.dao.TaskDao
import top.macaulish.pls.kits.JsonResponse
import top.macaulish.pls.pojo.TaskEntity
import top.macaulish.pls.pojo.UserEntity
import top.macaulish.pls.service.TaskService
import java.sql.Timestamp
import java.util.*


@Controller
@RequestMapping("/task")
class TaskController {

    private val log = Logger.getLogger(TaskController::class.java)

    @Autowired
    private lateinit var taskService:TaskService
    @Autowired
    private lateinit var taskDao:TaskDao


    @GetMapping(path=["/all"],produces = ["application/json;charset=UTF-8"])
    @ResponseBody
    fun getAllTasks():String{
        return try {
            val tasks = taskDao.queryByExample(TaskEntity())
            JsonResponse.success(tasks)
        }catch (e:Exception){
            log.error("wrong",e)
            JsonResponse.fail(e)
        }
    }

    @PostMapping(path=["/create/{userId}/{modelGuid}"])
    @ResponseBody
    fun createTask(@PathVariable userId:String,@PathVariable modelGuid:String,@RequestBody task:TaskEntity):String{
        try{

            val taskPrx = taskService.getTaskServer()
            task.guid = UUID.randomUUID().toString()
            //通过ICE调用服务端创建任务服务并解析结果
            val createBack = taskPrx.create(modelGuid,task.guid,task.taskType)
            val jsonCreate = JSONObject(createBack)
            if(jsonCreate.getString("result") != "success"){
                throw Exception(jsonCreate.getString("reason"))
            }
            if(taskService.isLocalHost()){
                task.saveHost = "127.0.0.1"
            }else{
                val jsonFTP = JSONObject(taskPrx.ftpInfo)
                val host = jsonFTP.getString("host")
                task.saveHost = host
            }
            task.createTime = Timestamp(System.currentTimeMillis())
//            task.creatorId = userId.toInt()

        }catch (e:Exception){
            return ""
        }

        return ""
    }


    @GetMapping(path=["/pojo"])
    fun testSpringMvc(): String {
        var user = UserEntity()
        user.username = "tom"
        user.guid = UUID.randomUUID().toString()
        return JsonResponse.success(user)
    }


    @GetMapping(path = ["/all.get"], produces = ["application/json;charset=UTF-8"])
    private fun queryAllTasks():List<TaskEntity>{
        TODO()
    }

}