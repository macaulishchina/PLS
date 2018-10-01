package top.macaulish.pls

import com.macaulish.top.coconut.util.DateKits
import org.junit.Before
import org.junit.Test
import top.macaulish.pls.kits.SFTPKits

/**
 *@author huyidong
 *@date 2018/10/1
 */
class TestSFTPKits {

    val host = "192.168.9.19"
    val port = 22
    val username = "yidong.hu"
    val password = "yidong.hu123"
    val timeout = 10000
    lateinit var ftp: SFTPKits

    @Before
    fun testLogin() {
        ftp = SFTPKits(host, username, password, port)
        print(ftp)
        ftp.login()
    }

    @Test
    fun testSFTPMethod() {
        println(ftp.pwd())
        ftp.cdParent()
        println(ftp.pwd())
        var files = ftp.ls()
        for (file in files) {
            print("\t$file")
        }
        ftp.cd("/home/yidong.hu/ftp")
        println(ftp.pwd())
        ftp.cdRoot()
        println(ftp.pwd())
        ftp.cd("")
        println(ftp.pwd())
        ftp.upload("/home/yidong.hu/ftp", "123.txt", "test".byteInputStream())
        ftp.cd("/home/yidong.hu")
        ftp.rmDir("ftp")
        files = ftp.ls("/")
        for (file in files) {
            print("\t$file")
        }
    }

    @Test
    fun testUploadAndDownload() {
        ftp.cd("/home/yidong.hu")
        ftp.mkDir("sftp")
        ftp.cd("sftp")
        ftp.upload("/home/yidong.hu/sftp", "321.txt", DateKits.getNow().byteInputStream())
        val res = ftp.downloadBytes("/home/yidong.hu/sftp", "321.txt") ?: throw Exception()
        println("Download file content = ${String(res)}")
    }

    @Test
    fun testExist() {
        println(ftp.pwd())
        println(ftp.exist("anaconda3"))
        println(ftp.existAbsolutelyFile("/usr/lib/libevm.so"))
        ftp.cd("/usr/lib")
        println(ftp.existFile("libevm.so"))
        ftp.cd(".")
        println(ftp.pwd())
        ftp.cd("..")
        println(ftp.pwd())
    }
}