package top.macaulish.pls.kits

import com.google.gson.Gson
import org.apache.log4j.Logger
import top.macaulish.pls.pojo.json.*

object JsonConverter {

    private val log = Logger.getLogger(JsonConverter::class.java)
    private val gson = Gson()

    fun fromActionBack(actionBack: String): ActionResultEntity {
        return try {
            gson.fromJson(actionBack, ActionResultEntity::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            log.error("Fail to convert from string $actionBack to Object ActionResultEntity", e)
            throw JsonConverterException(e)
        }
    }

    fun fromTaskInfo(taskInfo: String): TaskInfoEntity {
        return try {
            gson.fromJson(taskInfo, TaskInfoEntity::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            log.error("Fail to convert from string $taskInfo to Object ActionResultEntity", e)
            throw JsonConverterException(e)
        }
    }

    fun fromTaskProgress(taskProgress: String): TaskProcessEntity {
        return try {
            gson.fromJson(taskProgress, TaskProcessEntity::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            log.error("Fail to convert from string $taskProgress to Object ActionResultEntity", e)
            throw JsonConverterException(e)
        }
    }

    fun fromPath(taskPath: String): PathEntity {
        return try {
            gson.fromJson(taskPath, PathEntity::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            log.error("Fail to convert from string $taskPath to Object ActionResultEntity", e)
            throw JsonConverterException(e)
        }
    }

    fun fromFTPInfo(ftpInfo: String): FTPEntity {
        return try {
            gson.fromJson(ftpInfo, FTPEntity::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            log.error("Fail to convert from string $ftpInfo to Object ActionResultEntity", e)
            throw JsonConverterException(e)
        }
    }

    class JsonConverterException : Exception {

        constructor() : super("Fail to convert from string to object!")

        constructor(cause: Throwable) : super("Fail to convert from string to object!", cause)
    }
}