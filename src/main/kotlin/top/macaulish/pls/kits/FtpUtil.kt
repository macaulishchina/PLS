package top.macaulish.pls.kits

import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPReply
import java.io.*
import java.io.File.separatorChar
import java.net.SocketException


class FtpUtil {
    /**
     * 获取FTPClient对象
     *
     * @param ftpHost     FTP主机服务器
     * @param ftpPassword FTP 登录密码
     * @param ftpUserName FTP登录用户名
     * @param ftpPort     FTP端口 默认为21
     * @return
     */
    fun getFTPClient(ftpHost: String, ftpUserName: String,
                     ftpPassword: String, ftpPort: Int): FTPClient {
        var ftpClient = FTPClient()
        try {
            ftpClient = FTPClient()
            ftpClient.connect(ftpHost, ftpPort)// 连接FTP服务器
            ftpClient.login(ftpUserName, ftpPassword)// 登陆FTP服务器
            if (!FTPReply.isPositiveCompletion(ftpClient.replyCode)) {
                println("未连接到FTP，用户名或密码错误。")
                ftpClient.disconnect()
            } else {
                println("FTP连接成功。")
            }
        } catch (e: SocketException) {
            e.printStackTrace()
            println("FTP的IP地址可能错误，请正确配置。")
        } catch (e: IOException) {
            e.printStackTrace()
            println("FTP的端口错误,请正确配置。")
        }

        return ftpClient
    }

    /*
     * 从FTP服务器下载文件
     *
     * @param ftpHost FTP IP地址
     * @param ftpUserName FTP 用户名
     * @param ftpPassword FTP用户名密码
     * @param ftpPort FTP端口
     * @param ftpPath FTP服务器中文件所在路径 格式： ftptest/aa
     * @param localPath 下载到本地的位置 格式：H:/download
     * @param fileName 文件名称
     */
    fun downloadFtpFile(ftpHost: String, ftpUserName: String,
                        ftpPassword: String, ftpPort: Int, ftpPath: String, localPath: String,
                        fileName: String) {

        var ftpClient: FTPClient? = null

        try {
            ftpClient = getFTPClient(ftpHost, ftpUserName, ftpPassword, ftpPort)
            ftpClient.controlEncoding = "UTF-8" // 中文支持
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE)
            ftpClient.enterLocalPassiveMode()
            ftpClient.changeWorkingDirectory(ftpPath)

            val localFile = File(localPath + separatorChar + fileName)
            val os = FileOutputStream(localFile)
            ftpClient.retrieveFile(fileName, os)
            os.close()
            ftpClient.logout()

        } catch (e: FileNotFoundException) {
            println("没有找到" + ftpPath + "文件")
            e.printStackTrace()
        } catch (e: SocketException) {
            println("连接FTP失败.")
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
            println("文件读取错误。")
            e.printStackTrace()
        }

    }

    /**
     * Description: 向FTP服务器上传文件
     * @param ftpHost FTP服务器hostname
     * @param ftpUserName 账号
     * @param ftpPassword 密码
     * @param ftpPort 端口
     * @param ftpPath  FTP服务器中文件所在路径 格式： ftptest/aa
     * @param fileName ftp文件名称
     * @param input 文件流
     * @return 成功返回true，否则返回false
     */
    fun uploadFile(ftpHost: String, ftpUserName: String,
                   ftpPassword: String, ftpPort: Int, ftpPath: String,
                   fileName: String, input: InputStream): Boolean {
        var success = false
        var ftpClient: FTPClient? = null
        try {
            val reply: Int
            ftpClient = getFTPClient(ftpHost, ftpUserName, ftpPassword, ftpPort)
            reply = ftpClient.replyCode
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect()
                return success
            }
            ftpClient.controlEncoding = "UTF-8" // 中文支持
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE)
            ftpClient.enterLocalPassiveMode()
            ftpClient.changeWorkingDirectory(ftpPath)

            ftpClient.storeFile(fileName, input)

            input.close()
            ftpClient.logout()
            success = true
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (ftpClient!!.isConnected) {
                try {
                    ftpClient.disconnect()
                } catch (ioe: IOException) {
                }

            }
        }
        return success
    }
}