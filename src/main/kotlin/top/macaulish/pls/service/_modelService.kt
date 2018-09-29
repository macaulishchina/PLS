package top.macaulish.pls.service

import top.macaulish.pls.ice.client.ModelPrx

interface _modelService : _iceService{
    fun getModelServer(): ModelPrx
}