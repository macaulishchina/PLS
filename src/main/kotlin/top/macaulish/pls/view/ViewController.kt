package top.macaulish.pls.view

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import top.macaulish.pls.pojo.db.UserEntity
import javax.servlet.http.HttpServletRequest

/**
 *@author huyidong
 *@date 2018/10/10
 */
@Controller
@RequestMapping(produces = ["text/html"])
class ViewController {
    @Autowired(required = false)
    lateinit var request: HttpServletRequest

    @GetMapping(value = ["/upload/{taskGuid}"])
    fun uploadView(@PathVariable taskGuid: String): ModelAndView {
        val mv = ModelAndView()
        mv.addObject("taskGuid", taskGuid)
        mv.viewName = "upload"
        return mv
    }
}