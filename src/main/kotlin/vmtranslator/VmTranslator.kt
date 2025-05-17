package vmtranslator

import utils.FileUtils
import utils.cleanupLine
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
            .map { cleanupLine(it) }
            .filterNot { it.isEmpty() }
            .joinToString(System.lineSeparator()) { vmInstruction -> "// $vmInstruction${System.lineSeparator()}" + parser.toInstruction(vmInstruction).toAsmInstructions() }
    }

}