package top.macaulish.pls.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import top.macaulish.pls.dao.UserDao
import top.macaulish.pls.pojo.db.UserEntity

@Service
class UserService : _userService{

    @Autowired
    private lateinit var userDao:UserDao

    override fun login(username: String, password: String): Boolean {
        TODO()
    }

    override fun register(user: UserEntity): Boolean {
        TODO()
    }
}