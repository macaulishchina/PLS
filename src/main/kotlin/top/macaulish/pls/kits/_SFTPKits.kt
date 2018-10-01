package top.macaulish.pls.kits

import com.sun.org.apache.xpath.internal.operations.Bool
import org.apache.taglibs.standard.extra.spath.AbsolutePath
import java.io.InputStream

/**
 *@author huyidong
 *@date 2018/9/30
 */
interface _SFTPKits {

    fun login(): Boolean

    fun logout(): Boolean

    fun cd(dir: String): Boolean

    fun cdRoot(): Boolean

    fun cdParent(): Boolean

    fun pwd(): String

    fun rmDir(dir: String): Boolean

    fun rmFile(file: String): Boolean

    fun existAbsolutelyFile(absolutePath: String): Boolean

    fun exist(name: String): Boolean

    fun exist(dir: String, name: String): Boolean

    fun existDir(dir: String): Boolean

    fun existDir(pDir: String, dir: String): Boolean

    fun existFile(file: String): Boolean

    fun existFile(dir: String, file: String): Boolean

    fun ls(): Array<String>

    fun ls(dir: String): Array<String>

    fun lsFiles(): Array<String>

    fun lsFiles(dir: String): Array<String>

    fun lsDirs(): Array<String>

    fun lsDirs(dir: String): Array<String>

    fun mkDir(dir: String): Boolean

    fun upload(dir: String, saveFile: String, input: InputStream): Boolean

    fun upload(dir: String, saveFile: String, sourceFilePath: String): Boolean

    fun downloadTolocal(dir: String, file: String, savePath: String, saveName: String = file): Boolean

    fun downloadBytes(dir: String, file: String): ByteArray?


}