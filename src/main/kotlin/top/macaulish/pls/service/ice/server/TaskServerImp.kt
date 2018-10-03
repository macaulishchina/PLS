package top.macaulish.pls.service.ice.server

import com.zeroc.Ice.Current
import top.macaulish.pls.pojo.ice.*

/**
 *@author huyidong
 *@date 2018/10/3
 */
class TaskServerImp : Task {
    override fun create(modelGuid: String?, taskGuid: String?, taskType: String?, current: Current?): ActionBack {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun start(taskGuid: String?, current: Current?): ActionBack {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun pause(taskGuid: String?, current: Current?): ActionBack {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun resume(taskGuid: String?, current: Current?): ActionBack {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun stop(taskGuid: String?, current: Current?): ActionBack {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(taskGuid: String?, current: Current?): ActionBack {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun query(taskGuid: String?, current: Current?): TaskInfo {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getProgress(taskGuid: String?, current: Current?): TaskProcessInfo {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUploadDir(taskGuid: String?, current: Current?): PathInfo {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDownloadDir(taskGuid: String?, current: Current?): PathInfo {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDownloadResultZip(taskGuid: String?, current: Current?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFTPInfo(current: Current?): SFTPInfo {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}