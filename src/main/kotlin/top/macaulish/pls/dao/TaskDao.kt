package top.macaulish.pls.dao

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.orm.hibernate5.HibernateTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import top.macaulish.pls.pojo.db.TaskEntity

@Component
class TaskDao :_taskDao {


    @Autowired
    private lateinit var db: HibernateTemplate

    @Transactional(readOnly = false)
    override fun save(task: TaskEntity): Boolean {
        return try {
            db.save(task)
            true
        }catch (e:Exception){
            e.printStackTrace()
            false
        }
    }

    @Transactional(readOnly = false)
    override fun saveOrUpdate(task: TaskEntity): Boolean {
        return try {
            db.saveOrUpdate(task)
            true
        }catch (e:Exception){
            e.printStackTrace()
            false
        }
    }

    @Transactional(readOnly = false)
    override fun delete(task: TaskEntity): Boolean {
        return try {
            db.delete(task)
            true
        }catch (e:Exception){
            e.printStackTrace()
            false
        }
    }

    @Transactional(readOnly = false)
    override fun deleteByExample(task: TaskEntity): Boolean {
        return try{
            val tasks = db.findByExample(task)
            for(t in tasks){
                db.delete(t)
            }
            true
        }catch (e:Exception){
            e.printStackTrace()
            false
        }
    }

    @Transactional(readOnly = false)
    override fun update(task: TaskEntity): Boolean {
        return try{
            db.update(task)
            true
        }catch (e:Exception){
            e.printStackTrace()
            false
        }
    }

    @Transactional(readOnly = true)
    override fun queryFirstByExample(task: TaskEntity): TaskEntity? {
        val tasks = db.findByExample(task)
        return if(tasks.isEmpty()) null else tasks[0]
    }

    @Transactional(readOnly = true)
    override fun queryByExample(task: TaskEntity): List<TaskEntity> {
        return db.findByExample(task)
    }

    @Transactional(readOnly = true)
    override fun queryFirst(taskGuid: String): TaskEntity? {
        val ex = TaskEntity()
        ex.guid = taskGuid
        val tasks = db.findByExample(ex)
        return if (tasks.isEmpty()) null else tasks[0]
    }
}