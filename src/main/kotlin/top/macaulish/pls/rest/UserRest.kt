package top.macaulish.pls.rest

import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import top.macaulish.pls.dao.UserDao
import top.macaulish.pls.kits.JsonResponse as jr
import top.macaulish.pls.pojo.db.UserEntity
import top.macaulish.pls.service.UserService
import javax.servlet.http.HttpServletRequest

/**
 *@author huyidong
 *@date 2018/10/2
 */
@RestController
@RequestMapping(path = ["/user"], produces = ["application/json;charset=UTF-8"])
class UserRest {

    private val log = Logger.getLogger(UserRest::class.java)
    @Autowired(required = false)
    lateinit var request: HttpServletRequest
    @Autowired
    lateinit var userService: UserService
    @Autowired
    lateinit var userDao: UserDao

    @PostMapping(path = ["/register"])
    fun register(username: String, password: String): String {
        return try {
            var user = UserEntity()
            user.username = username
            user.password = password
            if (userService.register(user)) {
                user = userDao.queryFirstByExample(user) ?: return jr.fail("register successfully,but fail to save the register information")
                request.session.setAttribute("user", user)
                jr.success("$username register successfully")
            } else {
                jr.fail("fail to register with username $username")
            }
        } catch (e: Exception) {
            log.error("fail to register", e)
            jr.fail("internal error")
        }
    }

    @PostMapping(path = ["/login"])
    fun login(username: String, password: String): String {
        log.debug("#######$username  : $password")
        return try {
            if (userService.login(username, password)) {
                var user = UserEntity()
                user.username = username
                user = userDao.queryFirstByExample(user) ?: return jr.fail("login successfully,but fail to get the information from database")
                request.session.setAttribute("user", user)
                jr.success("login successfully")
            } else {
                jr.fail("fail to login with username $username")
            }
        } catch (e: Exception) {
            log.error("fail to login", e)
            jr.fail("internal error")
        }
    }

    @PostMapping(path = ["/logout"])
    fun logout(): String {
        return try {
            if (request.session.getAttribute("user") == null) {
                throw Exception("user information doesn't exist in the session")
            }
            request.session.removeAttribute("user")
            jr.success("logout successfully")
        } catch (e: Exception) {
            jr.fail("fail to logout")
        }
    }

}