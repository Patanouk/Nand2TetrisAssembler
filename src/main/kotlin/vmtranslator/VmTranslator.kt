package vmtranslator

import utils.isCommentLine
import utils.sanitizeLine
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
            .map { sanitizeLine(it) }
            .filterNot { isCommentLine(it) }
            .joinToString(System.lineSeparator()) { it + parser.toInstruction(it).toAsmInstructions() }
    }

}