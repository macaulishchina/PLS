package top.macaulish.pls.kits

import org.apache.commons.io.FileUtils
import org.apache.log4j.Logger
import java.io.File
import java.io.InputStream
import java.math.RoundingMode
import java.text.DecimalFormat

/**
 *@author huyidong
 *@date 2018/10/1
 */
object FileKits {

    private val log = Logger.getLogger(FileKits::class.java)

    fun saveFile(inputStream: InputStream, dir: String, fileName: String): Boolean {
        return try {
            FileUtils.copyInputStreamToFile(inputStream, File("$dir/$fileName"))
            true
        } catch (e: Exception) {
            log.error("fail to save file to $dir/$fileName", e)
            false
        }
    }

    fun getParentPath(path: String): String {
        val last = path.lastIndexOfAny(charArrayOf('\\', '/'))
        return if (last == -1) {
            "."
        } else if (last == 0) {
            "/"
        } else {
            path.substring(0, last)
        }
    }

    fun getFileName(path: String): String {
        val last = path.lastIndexOfAny(charArrayOf('\\', '/'))
        return if (last == -1) {
            path
        } else {
            path.substring(last + 1)
        }
    }

    /**
     * 获得文件[file]的大小，单位为字节
     * 如果文件不存在则返回 -1
     */
    fun getSizeCountByte(file: File): Long {
        return if (file.exists() && file.isFile) file.length() else -1
    }

    /**
     * 获得文件[file]的大小，返回方便阅读的字符串形式，自动选择合适的单位
     * 如果文件不存在则返回 "null"
     */
    fun getReadableSize(file: File): String {
        return toReadableSize(getSizeCountByte(file))
    }

    /**
     * 将一个单位为字节的长度[size]转换为方便阅读的形式
     * 单位可为Byte、KB、MB、GB、TB、PB或EB，自动选择合适的单位，例如：1.267GB
     * [size]小于0时返回 "null"
     */
    fun toReadableSize(size: Long): String {
        if (size < 0) return "null"
        var decimal = 0.0
        var integer = size
        var count = 0
        while (integer > 1024) {
            decimal = decimal / 1024 + integer % 1024 / 1024.0
            integer /= 1024
            count++
        }
        val format = DecimalFormat()
        format.maximumFractionDigits = 3
        format.roundingMode = RoundingMode.HALF_EVEN
        return format.format(decimal + integer) + when (count) {
            0 -> "Byte"
            1 -> "KB"
            2 -> "MB"
            3 -> "GB"
            4 -> "TB"
            5 -> "PB"
            6 -> "EB"
            else -> "??"
        }
    }

}
