package utils

import java.io.File

class FileUtils {

    companion object {
        fun getFileWithNewExtension(file: File, newExtension: String) = when(file.isFile) {
            true -> File("${file.parentFile.absolutePath}/${file.nameWithoutExtension}${newExtension}")
            false -> File("${file.parentFile.absolutePath}/out${newExtension}")
        }
    }

}