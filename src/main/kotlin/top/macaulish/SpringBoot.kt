package top.macaulish

import org.apache.log4j.Logger
import org.springframework.web.WebApplicationInitializer
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext
import javax.servlet.ServletContext
import org.springframework.web.servlet.DispatcherServlet


/**
 *@author huyidong
 *@date 2018/10/2
 */
class SpringBoot : WebApplicationInitializer {
    private val log = Logger.getLogger(SpringBoot::class.java)
    override fun onStartup(servletContext: ServletContext) {
        log.info("Spring boot -> onStartup called!!!!!!!!!!!!!!!!!!@")
        val ctx = AnnotationConfigWebApplicationContext()
        ctx.register(SpringBaseConfig::class.java, SpringMVCConfig::class.java)
        ctx.servletContext = servletContext
        val servlet = servletContext.addServlet("spring-mvc-dispatcher", DispatcherServlet(ctx))
        servlet.setLoadOnStartup(1)
        servlet.addMapping("/")
    }

}