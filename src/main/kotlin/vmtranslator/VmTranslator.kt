package vmtranslator

import utils.isCommentLine
import utils.removeComment
import java.io.File

class VmTranslator(private val vmInstructionFile: File) {

    private val parser = VmInstructionParser()

    fun writeToFile(): File {
        val outputFile = File("${vmInstructionFile.nameWithoutExtension}.asm")
        outputFile.writeBytes(writeToString().toByteArray())
        return outputFile
    }

    fun writeToString(): String {
        return vmInstructionFile.readLines()
            .map { removeComment(it) }
            .filterNot { isCommentLine(it) }
            .joinToString(System.lineSeparator()) { it + parser.toInstruction(it).toAsmInstructions() }
    }

}