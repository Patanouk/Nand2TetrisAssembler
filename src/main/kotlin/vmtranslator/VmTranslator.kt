package vmtranslator

import utils.FileUtils
import utils.cleanupLine
import java.io.File

class VmTranslator(private val vmInstructionFile: File) {

    private val parser = VmInstructionParser(vmInstructionFile.nameWithoutExtension)

    fun writeToFile(): File {
        val outputFile = FileUtils.getFileWithNewExtension(file = vmInstructionFile, newExtension = ".asm")

        when(vmInstructionFile.isFile) {
            true -> outputFile.writeBytes(writeToString(vmInstructionFile).toByteArray())
            false -> vmInstructionFile.listFiles().forEach {
                outputFile.writeBytes("// File : ${it.name}".toByteArray())
                outputFile.writeBytes(writeToString(it).toByteArray())
            }
        }

        outputFile.writeBytes(writeToString(vmInstructionFile).toByteArray())
        return outputFile
    }

    private fun writeToString(vmFile: File): String {
        return vmFile.readLines()
            .map { cleanupLine(it) }
            .filterNot { it.isEmpty() }
            .joinToString(System.lineSeparator()) { vmInstruction -> "// $vmInstruction${System.lineSeparator()}" + parser.toInstruction(vmInstruction).toAsmInstructions() }
    }

}