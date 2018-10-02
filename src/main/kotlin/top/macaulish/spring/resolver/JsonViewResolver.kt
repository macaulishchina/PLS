package top.macaulish.spring.resolver

import org.springframework.web.servlet.View
import org.springframework.web.servlet.ViewResolver
import org.springframework.web.servlet.view.json.MappingJackson2JsonView
import java.util.*

/**
 *@author huyidong
 *@date 2018/10/2
 */
class JsonViewResolver : ViewResolver {
    override fun resolveViewName(viewName: String, locale: Locale): View? {
        val view = MappingJackson2JsonView()
        view.setPrettyPrint(true)
        return view
    }
}