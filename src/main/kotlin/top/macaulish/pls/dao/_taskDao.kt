package top.macaulish.pls.dao

import top.macaulish.pls.pojo.db.TaskEntity


interface _taskDao {

    fun save(task: TaskEntity):Boolean

    fun saveOrUpdate(task: TaskEntity): Boolean

    fun delete(task: TaskEntity): Boolean

    fun deleteByExample(task: TaskEntity): Boolean

    fun update(task: TaskEntity): Boolean

    fun queryFirstByExample(task: TaskEntity): TaskEntity?

    fun queryByExample(task: TaskEntity): List<TaskEntity>

    fun queryFirst(taskGuid: String): TaskEntity?
}