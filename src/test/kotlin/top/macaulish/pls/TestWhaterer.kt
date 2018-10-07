package top.macaulish.pls

import com.google.gson.Gson
import org.apache.log4j.Logger
import org.junit.Test
import top.macaulish.pls.kits.FileKits
import top.macaulish.pls.pojo.json.ActionResultEntity
import java.net.InetAddress
import java.util.*

class TestWhaterer {

    private val log = Logger.getLogger(TestWhaterer::class.java)

    @Test
    fun testFileKits() {
        val path = "123.txt"
        println(FileKits.getParentPath(path))
        println(FileKits.getFileName(path))
    }



    @Test
    fun testLogger(){
        log.info("Hello world!")
    }

    @Test
    fun testGson(){
        val action = "{\n" +
                "            \"modelGuid\": \"5c64ee48-f81c-41ef-8c93-151aa745e88f\",\n" +
                "            \"action\": \"create\",\n" +
                "            \"result\": \"fail\",\n" +
                "            \"reason\": \"reason if fail\"\n" +
                "        }"
        val gson = Gson()
        val a = gson.fromJson(action, ActionResultEntity::class.java)
        print(a)

    }

    @Test
    fun testIP() {
        val ip = InetAddress.getLocalHost().hostAddress
        print(ip)
    }


    @Test
    fun testDog() {
        Dog()
    }

    class Dog() {

        init {
            eat()
        }

        fun eat() {
            print("eat")
        }
    }
    private class JsonObject(val state:String,var data:Any){
        fun toJsonString():String{
            return Gson().toJson(this)
        }
    }

    enum class Privilege(val n: Int) {
        GUEST(0), USER(1), SUPERUSER(2), ADMIN(9)
    }

    @Test
    fun testRandom() {

        log.debug(Random().nextInt(1))

    }


}