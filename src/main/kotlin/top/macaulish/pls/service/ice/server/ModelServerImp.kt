package top.macaulish.pls.service.ice.server

import com.zeroc.Ice.Current
import top.macaulish.pls.pojo.ice.ActionBack
import top.macaulish.pls.pojo.ice.ModelInfo

/**
 *@author huyidong
 *@date 2018/10/3
 */
class ModelServerImp : Model {
    override fun queryAll(current: Current?): Array<ModelInfo> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun querySpecific(modelGuid: String?, current: Current?): ModelInfo {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun startup(modelGuid: String?, current: Current?): ActionBack {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun shutdown(modelGuid: String?, current: Current?): ActionBack {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun reStartup(modelGuid: String?, current: Current?): ActionBack {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun consumeAbility(modelGuid: String?, current: Current?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}