package top.macaulish.pls.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import top.macaulish.pls.dao.UserDao
import top.macaulish.pls.kits.JsonResponse
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

    @Autowired
    lateinit var request: HttpServletRequest
    @Autowired
    lateinit var userService: UserService
    @Autowired
    lateinit var userDao: UserDao

    @PostMapping(path = ["/register"])
    fun register(username: String, password: String): String {
        var user = UserEntity()
        user.username = username
        user.password = password
        if (userService.register(user)) {
            user = userDao.queryFirstByExample(user) ?: return JsonResponse.fail("register successfully,but fail to save the register information")
            request.getSession(true).setAttribute("user", user)
            return JsonResponse.success("$username register successfully")
        } else {
            return JsonResponse.fail("fail to register with username $username")
        }
    }

    @PostMapping(path = ["/login"])
    fun login(username: String, password: String): String {
        if (userService.login(username, password)) {
            var user = UserEntity()
            user.username = username
            user = userDao.queryFirstByExample(user) ?: return JsonResponse.fail("login successfully,but fail to get the information from database")
            request.getSession(true).setAttribute("user", user)
            return JsonResponse.success("login successfully")
        } else {
            return JsonResponse.fail("fail to login with username $username")
        }
    }

    @PostMapping(path = ["/logout"])
    fun logout(): String {
        return try {
            if (request.session.getAttribute("user") == null) {
                throw Exception("user information doesn't exist in the session")
            }
            request.session.removeAttribute("user")
            JsonResponse.success("logout successfully")
        } catch (e: Exception) {
            JsonResponse.fail("fail to logout")
        }
    }

    @GetMapping(path = ["/currentUser"])
    fun getSessionUser(): String {
        return JsonResponse.success(request.session.getAttribute("user"))
    }
}