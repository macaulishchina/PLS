package top.macaulish.pls.service._interface

import top.macaulish.pls.pojo.ice.ModelInfo


interface _ModelService : _IceInterface {

    fun queryAllModels(): Array<ModelInfo>?

    fun queryModel(modelGuid: String): ModelInfo?

    fun startUpModel(modelGuid: String): Boolean

    fun shutdownModel(modelGuid: String): Boolean

    fun restartUpModel(modelGuid: String): Boolean

    fun queryConsumeAbility(modelGuid: String): Int
}