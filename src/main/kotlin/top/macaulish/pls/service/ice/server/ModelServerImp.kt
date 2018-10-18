package top.macaulish.pls.service.ice.server

import com.zeroc.Ice.Current
import top.macaulish.pls.pojo.ice.ActionBack
import top.macaulish.pls.pojo.ice.ModelInfo
import java.util.*

/**
 *@author huyidong
 *@date 2018/10/3
 */
class ModelServerImp : Model {

    val models = ArrayList<ModelInfo>()
    val m1 = ModelInfo()
    val m2 = ModelInfo()


    init {
        m1.name = "resnet 101"
        m1.description = "resnet 101 model"
        m1.guid = UUID.randomUUID().toString()
        m1.location = "localhost"
        m1.state = "new"
        m2.name = "resnet 50"
        m1.description = "resnet 50 model"
        m1.guid = UUID.randomUUID().toString()
        m1.location = "192.168.9.15"
        m1.state = "busy"
        models.add(m1)
        models.add(m2)
    }


    override fun queryAll(current: Current?): Array<ModelInfo> {
        return arrayOf(m1, m2)
    }

    override fun querySpecific(modelGuid: String?, current: Current?): ModelInfo {
        return m1
    }

    override fun consumeAbility(modelGuid: String?, current: Current?): Int {
        return 2
    }
}