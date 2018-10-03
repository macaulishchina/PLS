package top.macaulish.pls.service

import top.macaulish.pls.pojo.db.UserEntity


interface _UserService {

    enum class Privilege {
        GUEST,//匿名用户: 只读操作所有任务
        BAN,//黑名单用户：不能登入，没有任务操作权限
        USER,//一般用户：可以管理自己的任务，只读操作别人的任务
        SUPERUSER,//超级用户：可以管理所有任务
        ADMIN,//系统管理员：拥有系统所有权限
        ILLEGAL//未定义身份，不能登入，没有任务操作权限
    }

    fun getPrivilege(privilege: Int): Privilege

    fun login(username:String,password:String):Boolean

    fun register(user: UserEntity):Boolean
}