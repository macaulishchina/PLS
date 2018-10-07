package top.macaulish.pls.view

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import top.macaulish.pls.pojo.db.UserEntity
import javax.servlet.http.HttpServletRequest

/**
 *@author huyidong
 *@date 2018/10/4
 */
@Controller
@RequestMapping(value = ["/welcome"], produces = ["text/html"])
class WelcomeController {

    @Autowired(required = false)
    lateinit var request: HttpServletRequest

    @GetMapping
    fun welcome(): ModelAndView {
        val mv = ModelAndView()
        val user = request.session.getAttribute("user") as UserEntity?
        if (user == null) {
            mv.viewName = "login"
        } else {
            mv.viewName = "main"
        }
        return mv
    }


}