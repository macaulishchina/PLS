package top.macaulish

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportResource
import org.springframework.http.MediaType
import org.springframework.web.accept.ContentNegotiationManager
import org.springframework.web.multipart.commons.CommonsMultipartResolver
import org.springframework.web.servlet.ViewResolver
import org.springframework.web.servlet.config.annotation.*
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver
import org.springframework.web.servlet.view.InternalResourceViewResolver
import org.springframework.web.servlet.view.JstlView
import top.macaulish.spring.resolver.JsonViewResolver
import top.macaulish.spring.resolver.PdfViewResolver
import java.util.ArrayList

/**
 *@author huyidong
 *@date 2018/10/2
 */
@Configuration
@EnableWebMvc
@ImportResource(value = ["classpath:configs/spring-web.xml"])
class SpringMVCConfig : WebMvcConfigurationSupport() {

    /**
     * 设置由 web容器处理静态资源 ，相当于 xml中的<mvc:default-servlet-handler/>
     */
    override fun configureDefaultServletHandling(configurer: DefaultServletHandlerConfigurer) {
        configurer.enable()
    }

    /*
     * Configure ContentNegotiationManager
     */
    override fun configureContentNegotiation(configurer: ContentNegotiationConfigurer) {
        configurer.ignoreAcceptHeader(true)
                .defaultContentType(MediaType.TEXT_HTML)

    }

    /*
     * Configure ContentNegotiatingViewResolver
     */
    @Bean
    fun contentNegotiatingViewResolver(manager: ContentNegotiationManager): ViewResolver {
        val resolver = ContentNegotiatingViewResolver()
        resolver.order = 1
        resolver.contentNegotiationManager = manager
        val resolvers = ArrayList<ViewResolver>()

        resolvers.add(jsonViewResolver())
        resolvers.add(pdfViewResolver())
        resolvers.add(jspViewResolver())

        resolver.viewResolvers = resolvers
        return resolver
    }


    @Bean
    fun jsonViewResolver(): ViewResolver {
        return JsonViewResolver()
    }

    @Bean
    fun pdfViewResolver(): ViewResolver {
        return PdfViewResolver()
    }

    /*
     * Configure View resolver to provide HTML output This is the default format
     * in absence of any type suffix.
     */
    @Bean
    fun jspViewResolver(): ViewResolver {
        val viewResolver = InternalResourceViewResolver()
        viewResolver.order = 2
        viewResolver.setViewClass(JstlView::class.java)
        viewResolver.setPrefix("/WEB-INF/views/")
        viewResolver.setSuffix(".jsp")
        return viewResolver
    }

    @Bean
    fun multipartResolver(): CommonsMultipartResolver {
        val resolver = CommonsMultipartResolver()
        resolver.setMaxInMemorySize(10485760)
        resolver.setMaxInMemorySize(40960)
        resolver.setDefaultEncoding("UTF-8")
        return resolver
    }
}