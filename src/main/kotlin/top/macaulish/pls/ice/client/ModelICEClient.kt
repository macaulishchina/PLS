package top.macaulish.pls.ice.client

import com.zeroc.Ice.Util
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ModelICEClient {

    @Value("#{ice.ice_server_host}")
    private lateinit var iceHost: String

    @Value("#{ice.ice_server_port}")
    private lateinit var icePort: String

    @Value("#{ice.ice_server_model_name}")
    private lateinit var proxyIdentify: String

    fun getModelPrx(): ModelPrx {
        Util.initialize().use { communicator ->
            val base = communicator.stringToProxy("$proxyIdentify:default -h $iceHost -p $icePort")
            return ModelPrx.checkedCast(base) ?: throw Exception("Invalid proxy!")
        }
    }
}