package top.macaulish.pls

import com.zeroc.Ice.Communicator
import com.zeroc.Ice.Util
import jdk.nashorn.internal.runtime.regexp.joni.Config.log
import org.apache.log4j.Logger
import org.junit.Test
import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext
import top.macaulish.pls.dao.TaskDao
import top.macaulish.pls.pojo.db.TaskEntity
import top.macaulish.pls.service.ice.client.ModelPrx
import top.macaulish.pls.service.ice.server.ModelServerImp


class TestICE {

    lateinit var context:ApplicationContext

    @Test
    fun loadContext(){
        context = ClassPathXmlApplicationContext("classpath:/configs/spring-base.xml")
    }
    @Test
    fun test(){
        val taskDao = context.getBean("taskDao") as TaskDao
        val size = taskDao.queryByExample(TaskEntity()).size
        print("size *********$size")
    }


}

class Server {
    private val log = Logger.getLogger(Server::class.java)


    fun getModelPrx(): ModelPrx {
        try {
            var communicator = Util.initialize()
            val base = communicator.stringToProxy("Model:default -h 192.168.9.16  -p 10000")
            var modelPrx = ModelPrx.checkedCast(base) ?: throw Exception("Invalid proxy!")
            return modelPrx
        } catch (e: Exception) {
            log.error("Fail to connect to ice server ", e)
            throw e
        }
    }

    @Test
    fun runClientFactory() {
        log.info(getModelPrx().consumeAbility("12343"))
    }

    @Test
    fun runClient() {
        try {
            Util.initialize().use { communicator ->
                val base = communicator.stringToProxy("Model:default -h 192.168.9.16 -p 10000")
                var m = ModelPrx.checkedCast(base) ?: throw Exception("Invalid proxy!")
                print(m.consumeAbility("32324"))
            }
        } catch (e: Exception) {
            throw e
        }
    }

    @Test
    fun runServer() {
        try {
            val communicator = Util.initialize()
            val adapter =
                    communicator.createObjectAdapterWithEndpoints("SimplePrinterAdapter", "default -p 10000")
            val model = ModelServerImp()
            adapter.add(model, Util.stringToIdentity("pls_model"))
            adapter.activate()
            communicator.waitForShutdown()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}