package top.macaulish.pls

import com.google.gson.Gson
import org.apache.log4j.Logger
import org.junit.Test
import top.macaulish.pls.dao.entity.UserEntity
import java.util.*
import kotlin.collections.ArrayList

class TestWhaterer {

    private val log = Logger.getLogger(TestWhaterer::class.java)

    @Test
    fun testLogger(){
        log.info("Hello world!")
    }

    @Test
    fun testGson(){
        val user = UserEntity()
        user.username = "tom"
        user.password = "123"
        user.guid = UUID.randomUUID().toString()
        val user2 = UserEntity()
        user2.username = "tom2"
        user2.password = "1232"
        user2.guid = UUID.randomUUID().toString()
        var users = ArrayList<UserEntity>()
        users.add(user)
        users.add(user2)
        val j = JsonObject("success","tests").toJsonString()
        val u = JsonObject("success",user).toJsonString()
        val us = JsonObject("success",users).toJsonString()
        print(us)

    }

    private class JsonObject(val state:String,var data:Any){
        fun toJsonString():String{
            return Gson().toJson(this)
        }
    }
}