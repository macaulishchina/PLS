package top.macaulish.pls

import org.junit.Test
import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext
import top.macaulish.pls.dao.TaskDao
import top.macaulish.pls.dao.entity.TaskEntity


class TestICE {


    lateinit var context:ApplicationContext

    @Test
    fun loadContext(){
        context = ClassPathXmlApplicationContext("classpath:/configs/spring-base.xml")
    }
    @Test
    fun test(){
        val taskDao = context.getBean("taskDao") as TaskDao
        val size = taskDao.queryByExample(TaskEntity()).size
        print("size *********$size")
    }


}