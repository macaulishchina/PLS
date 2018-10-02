package top.macaulish.pls.kits

import com.jcraft.jsch.*
import org.apache.commons.io.IOUtils
import org.apache.log4j.Logger
import top.macaulish.pls.kits._interface._SFTPKits
import java.io.*
import java.util.*

/**
 *@author huyidong
 *@date 2018/9/30
 */
class SFTPKits(private val username: String, private val host: String, private val port: Int = 22, private val timeout: Int = 10000) : _SFTPKits {

    private val log = Logger.getLogger(SFTPKits::class.java)

    var password: String = ""
    var privateKey: String = ""
    lateinit var session: Session
    lateinit var sftp: ChannelSftp


    constructor(host: String, username: String, port: Int, privateKey: String) : this(username, host, port) {
        this.privateKey = privateKey
    }

    constructor(host: String, username: String, password: String, port: Int = 22) : this(username, host, port) {
        this.password = password
    }

    override fun login(): Boolean {
        return try {
            val jsch = JSch()
            if (privateKey.isNotBlank()) jsch.addIdentity(privateKey)
            session = jsch.getSession(username, host, port)
            if (password.isNotEmpty()) session.setPassword(password)
            val config = Properties()
            config["StrictHostKeyChecking"] = "no"
            session.setConfig(config)
            session.timeout = timeout
            session.connect()
            log.info("sftp session connected to $host:$port")
            log.info("opening channel")
            sftp = session.openChannel("sftp") as ChannelSftp
            sftp.connect()
            log.info("sftp connected successfully")
            true
        } catch (e: JSchException) {
            log.error("fail to connect to sftp", e)
            false
        }
    }


    override fun logout(): Boolean {
        return try {
            if (sftp.isClosed) {
                sftp.quit()
                sftp.disconnect()
            }
            if (session.isConnected) {
                session.disconnect()
            }
            true
        } catch (e: Exception) {
            log.error("fail to logout", e)
            false
        }
    }


    override fun cd(dir: String): Boolean {
        return if (dir.isBlank()) {
            log.error("invalid path")
            false
        } else {
            try {
                sftp.cd(dir.replace("\\\\".toRegex(), "/"))
                log.debug("change dir to $dir")
                true
            } catch (e: SftpException) {
                log.error("fail to change the dir", e)
                false
            }
        }
    }

    override fun cdRoot(): Boolean {
        var root = ""
        try {
            root = sftp.home
        } catch (e: SftpException) {
            log.error("fail to get root path", e)
        }
        return cd(root)
    }

    override fun cdParent(): Boolean {
        return cd("..")
    }

    override fun pwd(): String {
        return try {
            sftp.pwd()
        } catch (e: SftpException) {
            log.error("fail to get current direction", e)
            homeDir()
        }
    }

    override fun rmDir(dir: String): Boolean {
        if (!cd(dir)) return false

        val list: Vector<ChannelSftp.LsEntry>
        try {
            list = sftp.ls(sftp.pwd()) as Vector<ChannelSftp.LsEntry>
        } catch (e: SftpException) {
            log.error("can not list directory", e)
            return false
        }

        for (entry in list) {
            val fileName = entry.filename
            if (fileName != "." && fileName != "..") {
                if (entry.attrs.isDir) {
                    rmDir(fileName)
                } else {
                    rmFile(fileName)
                }
            }
        }

        if (!cdParent()) return false

        return try {
            sftp.rmdir(dir)
            log.debug("directory $dir successfully deleted")
            true
        } catch (e: SftpException) {
            log.error("failed to delete directory $dir", e)
            false
        }
    }

    override fun rmFile(file: String): Boolean {
        return if (file.isBlank()) {
            log.debug("invalid filename")
            false
        } else try {
            sftp.rm(file)
            log.debug("file $file successfully deleted")
            true
        } catch (e: SftpException) {
            log.error("failed to delete file $file", e)
            false
        }
    }

    override fun existAbsolutelyFile(absolutePath: String): Boolean {
        var exist = false
        val currentDir: String = pwd()

        val last = absolutePath.lastIndexOfAny(charArrayOf('\\', '/'))
        exist = if (last == -1) {
            existFile(absolutePath)
        } else {
            val pDir = absolutePath.substring(0, last)
            val file = absolutePath.substring(last + 1)
            cd(pDir)
            existFile(file)
        }
        cd(currentDir)
        return exist
    }

    override fun exist(name: String): Boolean {
        return exist(ls(), name)
    }

    override fun exist(dir: String, name: String): Boolean {
        return exist(ls(dir), name)
    }

    override fun existDir(dir: String): Boolean {
        return exist(lsDirs(), dir)
    }

    override fun existDir(pDir: String, dir: String): Boolean {
        return exist(lsDirs(pDir), dir)
    }

    override fun existFile(file: String): Boolean {
        return exist(lsFiles(), file)
    }

    override fun existFile(dir: String, file: String): Boolean {
        return exist(lsDirs(dir), file)
    }

    override fun ls(): Array<String> {
        return list(Filter.ALL)
    }

    override fun ls(dir: String): Array<String> {
        if (!cd(dir)) return arrayOf()
        return list(Filter.ALL)
    }

    override fun lsFiles(): Array<String> {
        return list(Filter.FILE)
    }

    override fun lsFiles(dir: String): Array<String> {
        if (!cd(dir)) return arrayOf()
        return list(Filter.FILE)
    }

    override fun lsDirs(): Array<String> {
        return list(Filter.DIR)
    }

    override fun lsDirs(dir: String): Array<String> {
        if (!cd(dir)) return arrayOf()
        return list(Filter.DIR)
    }

    override fun mkDir(dir: String): Boolean {
        return try {
            sftp.mkdir(dir)
            log.debug("directory successfully created,dir=$dir")
            true
        } catch (e: SftpException) {
            log.error("failed to create directory", e)
            false
        }
    }

    /**
     * 将输入流的数据[input]上传到sftp作为文件。
     * 文件所在目录[dir]
     * 保存文件名为[saveFile]
     */
    override fun upload(dir: String, saveFile: String, input: InputStream): Boolean {
        val currentDir: String = pwd()
        if (!cd(dir)) return false

        return try {
            sftp.put(input, saveFile, ChannelSftp.OVERWRITE)
            if (!existFile(saveFile)) {
                log.debug("upload file $saveFile failed")
                false
            }
            log.debug("upload file $saveFile to $dir/$saveFile successfully")
            true
        } catch (e: SftpException) {
            log.error("fail to upload file $saveFile", e)
            false
        } finally {
            cd(currentDir)
        }
    }

    /**
     * 本地文件[sourceFilePath]上传到远程主机目录[dir]下保存文件名为[saveFile]
     */
    override fun upload(dir: String, saveFile: String, sourceFilePath: String): Boolean {
        val currentDir: String = pwd()
        if (!cd(dir)) return false
        return try {
            sftp.put(sourceFilePath, saveFile, ChannelSftp.OVERWRITE)
            if (!existFile(saveFile)) {
                log.debug("upload file $saveFile failed")
                false
            }
            log.debug("upload file $saveFile to $dir/$saveFile successfully")
            true
        } catch (e: SftpException) {
            log.error("fail to upload file $saveFile", e)
            false
        } finally {
            cd(currentDir)
        }
    }

    override fun downloadTolocal(dir: String, file: String, savePath: String, saveName: String): Boolean {
        val currentDir: String = pwd()
        if (!cd(dir)) return false
        return try {
            val localSave = savePath + File.separator + saveName
            sftp.get(file, localSave)
            if (!File(localSave).exists()) {
                log.debug("download file failed")
                false
            }
            true
        } catch (e: Exception) {
            log.error("fail to download file from $dir/$file", e)
            false
        } finally {
            cd(currentDir)
        }

    }

    override fun downloadBytes(dir: String, file: String): ByteArray? {
        val currentDir: String = pwd()
        if (!cd(dir)) return null
        return try {
            IOUtils.toByteArray(sftp.get(file))
        } catch (e: Exception) {
            log.error("fail to download file from $dir/$file", e)
            null
        } finally {
            cd(currentDir)
        }
    }


    /**
     * 枚举，用于过滤文件和文件夹
     */
    private enum class Filter {
        /*
         * 文件及文件夹
         */
        ALL,
        /*
         * 文件
         */
        FILE,
        /*
         * 文件夹
         */
        DIR
    }

    /**
     * 列出当前目录下的文件及文件夹
     */
    private fun list(filter: Filter): Array<String> {
        val list: Vector<ChannelSftp.LsEntry>
        try {
            //ls方法会返回两个特殊的目录，当前目录(.)和父目录(..)
            list = sftp.ls(sftp.pwd()) as Vector<ChannelSftp.LsEntry>
        } catch (e: SftpException) {
            log.error("can not list directory", e)
            return arrayOf()
        }

        val resultList = ArrayList<String>()
        for (entry in list) {
            if (filter(entry, filter)) {
                resultList.add(entry.filename)
            }
        }
        return resultList.toTypedArray()
    }


    private fun filter(entry: ChannelSftp.LsEntry, f: Filter): Boolean {
        return when (f) {
            Filter.ALL -> entry.filename != "." && entry.filename != ".."
            Filter.FILE -> entry.filename != "." && entry.filename != ".." && !entry.attrs.isDir
            Filter.DIR -> entry.filename != "." && entry.filename != ".." && entry.attrs.isDir
        }
    }


    private fun homeDir(): String {
        return try {
            sftp.home
        } catch (e: Exception) {
            "/"
        }

    }

    private fun exist(strArr: Array<String>, str: String): Boolean {
        if (strArr.isEmpty()) return false
        if (str.isBlank()) return false

        for (s in strArr) {
            if (s.equals(str, ignoreCase = true)) {
                return true
            }
        }
        return false
    }

    override fun toString(): String {
        return "$username@$host:$port -p $password\n"
    }

}