package top.macaulish.pls.service

import top.macaulish.pls.pojo.json.ModelEntity


interface _modelService : _iceInterface {

    fun queryAllModels(): List<ModelEntity>

    fun queryModel(modelGuid: String): ModelEntity?

    fun startUpModel(modelGuid: String): Boolean

    fun stutdownModel(modelGuid: String): Boolean

    fun restartUpModel(modelGuid: String): Boolean

    fun queryConsumeAbility(modelGuid: String): Int
}