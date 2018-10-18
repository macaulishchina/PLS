package top.macaulish.pls

import org.apache.log4j.Logger
import org.junit.Before
import org.junit.Test
import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext
import top.macaulish.pls.dao.TaskDao
import top.macaulish.pls.dao.UserDao
import top.macaulish.pls.pojo.db.UserEntity
import top.macaulish.pls.service.UserService

/**
 *@author huyidong
 *@date 2018/10/16
 */
class Login {
    private val log = Logger.getLogger(Login::class.java)
    lateinit var context: ApplicationContext

    @Before
    fun loadContext() {
        context = ClassPathXmlApplicationContext("classpath:/configs/spring-base.xml")
    }

    @Test
    fun login() {
        val userService = context.getBean("userService") as UserService
        val user = UserEntity()
        user.username = "admin"
        user.password = "admin"
        val r = userService.login("user", "user")
        log.info("login result:$r")
    }

    @Test
    fun queryAllUsers() {
        var userDao = context.getBean("userDao") as UserDao
        val users = userDao.queryByExample(UserEntity())
        for (user in users) {
            log.info("${user.username}:${user.password}:${user.privilege}\n")
        }
    }
}