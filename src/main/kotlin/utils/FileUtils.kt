package utils

import java.io.File

class FileUtils {

    companion object {
        fun getFileWithNewExtension(file: File, newExtension: String) = File("${file.parentFile.absolutePath}/${file.nameWithoutExtension}${newExtension}")
    }

}