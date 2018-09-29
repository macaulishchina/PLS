package top.macaulish.pls.service

import top.macaulish.pls.ice.client.TaskPrx

interface _taskService :_iceService{
    fun getTaskServer(): TaskPrx

}