package top.macaulish.pls

import org.junit.Test
import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext


class TestSpringMVC {

    lateinit var context:ApplicationContext

    @Test
    fun loadContext(){
        context = ClassPathXmlApplicationContext("classpath:/configs/spring-base.xml")
    }
}