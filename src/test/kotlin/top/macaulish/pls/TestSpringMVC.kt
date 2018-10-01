package top.macaulish.pls

import org.junit.Before
import org.junit.Test
import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext
import top.macaulish.pls.dao.TaskDao


class TestSpringMVC {

    lateinit var context:ApplicationContext

    @Before
    fun loadContext(){
        context = ClassPathXmlApplicationContext("classpath:/configs/spring-base.xml")
    }

    @Test
    fun testDB() {
        val taskDao = context.getBean("taskDao") as TaskDao
        val task = taskDao.queryFirst("cf185db2-c579-11e8-8656-f8a96341e199")
        print(task)
    }
}