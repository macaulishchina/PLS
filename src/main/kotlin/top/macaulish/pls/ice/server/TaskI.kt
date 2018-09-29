package top.macaulish.pls.ice.server

import com.zeroc.Ice.Current
import org.apache.log4j.Logger

class TaskI : Task {

    private val log = Logger.getLogger(TaskI::class.java)

    override fun getDownloadDir(taskGuid: String?, current: Current?): String {
        log.info("ICE test server "+taskGuid)
        return "test"
    }

}