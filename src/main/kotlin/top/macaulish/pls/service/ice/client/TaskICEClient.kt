package top.macaulish.pls.service.ice.client

import com.zeroc.Ice.Util
import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class TaskICEClient {

    private val log = Logger.getLogger(TaskICEClient::class.java)

    @Value("#{ice.ice_server_host}")
    private lateinit var iceHost: String

    @Value("#{ice.ice_server_port}")
    private lateinit var icePort: String

    @Value("#{ice.ice_server_task_name}")
    private lateinit var proxyIdentify: String

    fun getTaskPrx(): TaskPrx {

        return try {
            val communicator = Util.initialize()
            val base = communicator.stringToProxy("$proxyIdentify:default -h $iceHost  -p $icePort")
            TaskPrx.checkedCast(base) ?: throw Exception("Invalid proxy!")
        } catch (e: Exception) {
            log.error("Fail to connect to ice server @$iceHost:$icePort of identified string $proxyIdentify", e)
            throw e
        }
    }


}