package top.macaulish.pls.service

import netscape.security.Privilege
import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import top.macaulish.pls.dao.UserDao
import top.macaulish.pls.pojo.db.UserEntity
import java.util.*

@Service
class UserService : _UserService {


    private val log = Logger.getLogger(UserService::class.java)

    @Autowired
    private lateinit var userDao:UserDao

    override fun login(username: String, password: String): Boolean {
        return try {
            val ex = UserEntity()
            ex.username = username
            val user = userDao.queryFirstByExample(ex)
                    ?: throw Exception("fail to login,username $username doesn't exist")
            if (user.password != password) throw Exception("fail to login,password isn't correct")
            when (getPrivilege(user.privilege)) {
                _UserService.Privilege.ILLEGAL -> throw Exception("illegal privilege number")
                _UserService.Privilege.BAN -> throw Exception("sorry,your account has't been banned")
                else -> true
            }
        } catch (e: Exception) {
            log.error("fail to login", e)
            false
        }
    }

    override fun register(user: UserEntity): Boolean {
        return try {
            if (user.username == null || user.username.isBlank() || user.password == null)
                throw Exception("lack necessary information to register a user")
            val ex = UserEntity()
            ex.username = user.username
            val quser = userDao.queryFirstByExample(ex)
            if (quser != null) throw Exception("fail to register,the username has existed")
            user.privilege = 1
            user.guid = UUID.randomUUID().toString()
            userDao.save(user)
            true
        } catch (e: Exception) {
            log.error("fail to register", e)
            false
        }

    }

    override fun getPrivilege(privilege: Int): _UserService.Privilege {
        return when (privilege) {
            -1 -> _UserService.Privilege.BAN         //黑名单用户：不能登入，没有任务操作权限
            0 -> _UserService.Privilege.GUEST       //匿名用户
                1 -> _UserService.Privilege.USER        //一般用户：可以管理自己的任务，只读操作别人的任务
                2 -> _UserService.Privilege.SUPERUSER   //超级用户：可以管理所有任务
                9 -> _UserService.Privilege.ADMIN       //系统管理员：拥有系统所有权限
            else -> _UserService.Privilege.ILLEGAL    //未定义身份:不能登入，没有任务操作权限
            }
    }

    fun isLegalUser(user: UserEntity): Boolean {
        return when (getPrivilege(user.privilege)) {
            _UserService.Privilege.USER,
            _UserService.Privilege.SUPERUSER,
            _UserService.Privilege.ADMIN -> true
            else -> false
        }
    }

    fun isSuperUser(user: UserEntity): Boolean {
        return when (getPrivilege(user.privilege)) {
            _UserService.Privilege.SUPERUSER,
            _UserService.Privilege.ADMIN -> true
            else -> false
        }
    }

    fun isAdministrater(user: UserEntity): Boolean {
        return when (getPrivilege(user.privilege)) {
            _UserService.Privilege.ADMIN -> true
            else -> false
        }
    }
}