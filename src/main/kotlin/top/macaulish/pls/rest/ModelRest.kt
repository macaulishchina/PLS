package top.macaulish.pls.rest

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 *@author huyidong
 *@date 2018/10/2
 */
@RestController
@RequestMapping(path = ["/{userGuid}/model"], produces = ["application/json;charset=UTF-8"])
class ModelRest {
}