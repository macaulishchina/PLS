package top.macaulish.pls.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import top.macaulish.pls.dao.UserDao
import top.macaulish.pls.pojo.db.UserEntity
import top.macaulish.pls.kits.JsonResponse as jr
import top.macaulish.pls.service.ModelService
import top.macaulish.pls.service.UserService

/**
 *@author huyidong
 *@date 2018/10/2
 */
@RestController
@RequestMapping(path = ["/model"], produces = ["application/json;charset=UTF-8"])
class ModelRest {

    @Autowired
    private lateinit var modelService: ModelService
    @Autowired
    private lateinit var userDao: UserDao
    @Autowired
    private lateinit var userService: UserService

    @GetMapping(value = ["/query"])
    fun queryAll(): String {
        return modelService.queryAllModels().let { if (it == null) jr.fail("failed to query models") else jr.success(it) }
    }

    @GetMapping(value = ["/query/{modelGuid}"])
    fun queryOne(@PathVariable modelGuid: String): String {
        return modelService.queryModel(modelGuid).let { if (it == null) jr.fail("failed to query model") else jr.success(it) }
    }

    @GetMapping(value = ["/ability/{modelGuid}"])
    fun queryAbility(@PathVariable modelGuid: String): String {
        return modelService.queryConsumeAbility(modelGuid).let { if (it == -1) jr.fail("failed to query the model") else jr.success(it) }
    }

    @PostMapping(value = ["/{username}/startup/{modelGuid}"])
    fun startupModel(@PathVariable username: String, @PathVariable modelGuid: String): String {
        val ex = UserEntity()
        ex.username = username
        val user = userDao.queryFirstByExample(ex)
        return if (user == null) {
            jr.fail("the user $username doesn't exist")
        } else {
            if (userService.isLegalUser(user)) {
                if (modelService.startUpModel(modelGuid)) {
                    jr.success("startup the model successfully")
                } else {
                    jr.fail("fail to startup the model")
                }
            } else {
                jr.fail("permission denied")
            }
        }
    }

    @PostMapping(value = ["/{username}/shutdown/{modelGuid}"])
    fun shutdownModel(@PathVariable username: String, @PathVariable modelGuid: String): String {
        val ex = UserEntity()
        ex.username = username
        val user = userDao.queryFirstByExample(ex)
        return if (user == null) {
            jr.fail("the user $username doesn't exist")
        } else {
            if (userService.isSuperUser(user)) {
                if (modelService.shutdownModel(modelGuid)) {
                    jr.success("shutdown the model successfully")
                } else {
                    jr.fail("fail to shutdown the model")
                }
            } else {
                jr.fail("permission denied")
            }
        }
    }

    @PostMapping(value = ["/{username}/restartup/{modelGuid}"])
    fun reStartupModel(@PathVariable username: String, @PathVariable modelGuid: String): String {
        val ex = UserEntity()
        ex.username = username
        val user = userDao.queryFirstByExample(ex)
        return if (user == null) {
            jr.fail("the user $username doesn't exist")
        } else {
            if (userService.isSuperUser(user)) {
                if (modelService.restartUpModel(modelGuid)) {
                    jr.success("re-startup the model successfully")
                } else {
                    jr.fail("fail to re-startup the model")
                }
            } else {
                jr.fail("permission denied")
            }
        }
    }

}