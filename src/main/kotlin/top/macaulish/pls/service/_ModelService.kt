package top.macaulish.pls.service

import top.macaulish.pls.pojo.ice.ModelInfo
import top.macaulish.pls.service._IceInterface


interface _ModelService : _IceInterface {

    fun queryAllModels(): Array<ModelInfo>?

    fun queryModel(modelGuid: String): ModelInfo?

    fun startUpModel(modelGuid: String): Boolean

    fun shutdownModel(modelGuid: String): Boolean

    fun restartUpModel(modelGuid: String): Boolean

    fun queryConsumeAbility(modelGuid: String): Int

    fun getSourceTypes(): Array<String>

    fun getTargetType(): Array<String>
}