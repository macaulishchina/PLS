package top.macaulish.pls.service

import com.zeroc.Ice.Util
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import top.macaulish.pls.ice.client.TaskPrx

@Service
class TaskService : _taskService {

    @Value("#{ice.ice_server_host}")
    private lateinit var iceHost:String

    @Value("#{ice.ice_server_port}")
    private lateinit var icePort:String

    @Value("#{ice.ice_server_task_name}")
    private lateinit var proxyIdentify:String

    @Value("#{ice.ice_server_if_local}")
    private  var localHost: Boolean = true

    override fun getTaskServer(): TaskPrx {
        Util.initialize().use { communicator ->
            val base = communicator.stringToProxy("$proxyIdentify:default -h $iceHost -p $icePort")
            return TaskPrx.checkedCast(base) ?: throw Exception("Invalid proxy!")
        }
    }

    override fun isLocalHost(): Boolean {
        return localHost
    }
}