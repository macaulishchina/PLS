package top.macaulish.pls

import org.apache.log4j.Logger
import org.junit.Before
import org.junit.Test
import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext
import top.macaulish.pls.dao.TaskDao
import top.macaulish.pls.pojo.db.UserEntity
import top.macaulish.pls.service.UserService

/**
 *@author huyidong
 *@date 2018/10/16
 */
class Register {
    private val log = Logger.getLogger(Register::class.java)
    lateinit var context: ApplicationContext

    @Before
    fun loadContext() {
        context = ClassPathXmlApplicationContext("classpath:/configs/spring-base.xml")
    }

    @Test
    fun registerAdmin() {
        val userService = context.getBean("userService") as UserService
        val user = UserEntity()
        user.username = "admin"
        user.password = "admin"
        user.privilege = 9
        val r = userService.register(user)
        log.info("register result:$r")
    }
}