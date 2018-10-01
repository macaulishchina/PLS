package top.macaulish.pls.kits

import java.io.*


class TestFTP {
    object test {
        @JvmStatic
        fun main(args: Array<String>) {
            val ftpHost = "192.168.9.19"
            val ftpUserName = "yidong.hu"
            val ftpPassword = "yidong.hu123"
            val ftpPort = 21
            val ftpPath = "/home/yidong.hu/ftp"
            val localPath = "D:\\123.txt"
            val fileName = "123.txt"

            //上传一个文件
            try {
                val `in` = FileInputStream(File(localPath))
                val test = FtpUtil().uploadFile(ftpHost, ftpUserName, ftpPassword, ftpPort, ftpPath, fileName, `in`)
                println(test)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                println(e)
            }

            /*
            //在FTP服务器上生成一个文件，并将一个字符串写入到该文件中
            try {
                val input = ByteArrayInputStream("test ftp jyf".toByteArray(charset("GBK")))
                val flag = FtpUtil().uploadFile(ftpHost, ftpUserName, ftpPassword, ftpPort, ftpPath, fileName, input)
                println(flag)
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }

            //下载一个文件
            FtpUtil().downloadFtpFile(ftpHost, ftpUserName, ftpPassword, ftpPort, ftpPath, localPath, fileName)
            */
        }
    }
}