package top.macaulish.pls.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import top.macaulish.pls.ice.client.ModelICEClient
import top.macaulish.pls.pojo.json.ModelEntity

@Service
class ModelService : _modelService{

    @Autowired
    private lateinit var modelClient: ModelICEClient

    override fun queryAllModels(): List<ModelEntity> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun queryModel(modelGuid: String): ModelEntity? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun startUpModel(modelGuid: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun stutdownModel(modelGuid: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun restartUpModel(modelGuid: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun queryConsumeAbility(modelGuid: String): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}