package top.macaulish.pls.view

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

/**
 *@author huyidong
 *@date 2018/9/25
 */
@Controller
class HelloController {

    @GetMapping(path = ["/hello"])
    fun helloWorld():String{
        return "Hello"
    }
}