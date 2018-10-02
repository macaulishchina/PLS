package top.macaulish.spring.resolver

import org.springframework.web.servlet.View
import org.springframework.web.servlet.ViewResolver
import top.macaulish.spring.view.PdfViewTemplate
import java.util.*

/**
 *@author huyidong
 *@date 2018/10/2
 */
class PdfViewResolver : ViewResolver {
    override fun resolveViewName(viewName: String, locale: Locale): View? {
        return PdfViewTemplate()
    }
}