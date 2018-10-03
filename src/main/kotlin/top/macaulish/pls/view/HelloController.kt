package top.macaulish.pls.view

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import top.macaulish.pls.kits.JsonResponse as jr

/**
 *@author huyidong
 *@date 2018/9/25
 */
@Controller
class HelloController {

    @GetMapping(path = ["/hi"])
    @ResponseBody
    fun test(): String {
        return "Hello"
    }


    @GetMapping(path = ["/hello"])
    fun helloWorld():String{
        return "Hello"
    }


    @GetMapping(path = ["/dog"])
    @ResponseBody
    fun helloDogGet(): String {
        return "{      \"name\":\"tom\",      \"weight\":\"12.3kg\"      } "
    }

    @PostMapping(path = ["/dog"], consumes = ["application/json;charset=UTF-8"], produces = ["application/json;charset=UTF-8"])
    @ResponseBody
    fun helloDogPost(@RequestBody d: Dog): String {
        return jr.success(d.toString())
    }

    class Dog {
        var name = ""
        var weight = ""
        override fun toString(): String {
            return "This is a dog called $name of weight $weight."
        }
    }
}