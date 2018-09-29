package top.macaulish.pls.control

import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import top.macaulish.pls.dao.TaskDao
import top.macaulish.pls.service.TaskService


@RestController
@RequestMapping("/model")
class ControllerModel {

    private val log = Logger.getLogger(ControllerModel::class.java)

    @Autowired
    private lateinit var taskService: TaskService
    @Autowired
    private lateinit var taskDao: TaskDao


}