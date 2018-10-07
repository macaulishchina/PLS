package top.macaulish.pls.service

import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import top.macaulish.pls.service.ice.client.ModelICEClient
import top.macaulish.pls.pojo.ice.ModelInfo

@Service
class ModelService : _ModelService {

    private val log = Logger.getLogger(ModelService::class.java)

    @Autowired
    private lateinit var modelClient: ModelICEClient

    override fun queryAllModels(): Array<ModelInfo> {
        return try {
            modelClient.getModelPrx().queryAll()
        } catch (e: Exception) {
            log.error("查询模型信息失败！", e)
            emptyArray()
        }
    }

    override fun queryModel(modelGuid: String): ModelInfo? {
        return try {
            modelClient.getModelPrx().querySpecific(modelGuid)
        } catch (e: Exception) {
            log.error("查询模型信息失败！Guid=$modelGuid", e)
            null
        }
    }

    override fun startUpModel(modelGuid: String): Boolean {
        TODO()
    }

    override fun shutdownModel(modelGuid: String): Boolean {
        TODO()
    }

    override fun restartUpModel(modelGuid: String): Boolean {
        TODO()
    }

    override fun queryConsumeAbility(modelGuid: String): Int {
        return try {
            modelClient.getModelPrx().consumeAbility(modelGuid)
        } catch (e: Exception) {
            log.error("查询可用资源失败！", e)
            -1
        }
    }

}