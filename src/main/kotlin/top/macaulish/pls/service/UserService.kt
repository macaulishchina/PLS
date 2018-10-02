package top.macaulish.pls.service

import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import top.macaulish.pls.dao.UserDao
import top.macaulish.pls.pojo.db.UserEntity
import top.macaulish.pls.service._interface._UserService

@Service
class UserService : _UserService {


    private val log = Logger.getLogger(UserService::class.java)

    @Autowired
    private lateinit var userDao:UserDao

    override fun login(username: String, password: String): Boolean {
        TODO()
    }

    override fun register(user: UserEntity): Boolean {
        TODO()
    }

    override fun getPrivilege(userGuid: String): _UserService.Privilege {
        return try {
            val user = userDao.queryFirst(userGuid) ?: return _UserService.Privilege.GUEST
            val privilege = user.privilege
            when (privilege) {
                0 -> _UserService.Privilege.BAN         //黑名单用户：不能登入，没有任务操作权限
                1 -> _UserService.Privilege.USER        //一般用户：可以管理自己的任务，只读操作别人的任务
                2 -> _UserService.Privilege.SUPERUSER   //超级用户：可以管理所有任务
                9 -> _UserService.Privilege.ADMIN       //系统管理员：拥有系统所有权限
                else -> _UserService.Privilege.GUEST    //匿名用户: 仅可以查看任务
            }
        } catch (e: Exception) {
            log.error("fail to get the user privilege", e)
            _UserService.Privilege.GUEST
        }

    }
}