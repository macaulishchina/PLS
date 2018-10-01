package top.macaulish.pls.service

import top.macaulish.pls.pojo.ice.ModelInfo


interface _modelService : _iceInterface {

    fun queryAllModels(): Array<ModelInfo>?

    fun queryModel(modelGuid: String): ModelInfo?

    fun startUpModel(modelGuid: String): Boolean

    fun shutdownModel(modelGuid: String): Boolean

    fun restartUpModel(modelGuid: String): Boolean

    fun queryConsumeAbility(modelGuid: String): Int
}