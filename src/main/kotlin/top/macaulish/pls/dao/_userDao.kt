package top.macaulish.pls.dao

import top.macaulish.pls.pojo.UserEntity


interface _userDao{

    fun save(user: UserEntity):Boolean

    fun saveOrUpdate(user: UserEntity):Boolean

    fun delete(user: UserEntity):Boolean

    fun deleteByExample(user: UserEntity):Boolean

    fun update(user: UserEntity):Boolean

    fun queryFirstByExample(user: UserEntity):UserEntity?

    fun queryByExample(user: UserEntity):List<UserEntity>
}