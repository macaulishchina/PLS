package top.macaulish.pls

import com.google.gson.Gson
import org.apache.log4j.Logger
import org.junit.Test
import top.macaulish.pls.kits.JsonConverter
import top.macaulish.pls.pojo.json.ActionResultEntity

class TestWhaterer {

    private val log = Logger.getLogger(TestWhaterer::class.java)

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

    private class JsonObject(val state:String,var data:Any){
        fun toJsonString():String{
            return Gson().toJson(this)
        }
    }
}