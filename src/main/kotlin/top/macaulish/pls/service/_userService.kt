package top.macaulish.pls.service

import top.macaulish.pls.pojo.db.UserEntity


interface _userService {

    fun login(username:String,password:String):Boolean

    fun register(user: UserEntity):Boolean
}