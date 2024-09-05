package vmtranslator

import utils.FileUtils
import utils.isCommentLine
import utils.removeComment
import java.io.File

class VmTranslator(private val vmInstructionFile: File) {

    private val parser = VmInstructionParser(vmInstructionFile.nameWithoutExtension)

    fun writeToFile(): File {
        val outputFile = FileUtils.getFileWithNewExtension(file = vmInstructionFile, newExtension = ".asm")
        outputFile.writeBytes(writeToString().toByteArray())
        return outputFile
    }

    fun writeToString(): String {
        return vmInstructionFile.readLines()
            .map { removeComment(it) }
            .filterNot { isCommentLine(it) }
            .joinToString(System.lineSeparator()) { vmInstruction -> "// $vmInstruction${System.lineSeparator()}" + parser.toInstruction(vmInstruction).toAsmInstructions() }
    }

}