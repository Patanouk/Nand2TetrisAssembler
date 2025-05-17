package vmtranslator

import utils.FileUtils
import utils.cleanupLine
import java.io.File

class VmTranslator(private val vmInstructionFile: File) {

    private val parser = VmInstructionParser(vmInstructionFile.nameWithoutExtension)

    fun writeToFile(): File {
        val outputFile = FileUtils.getFileWithNewExtension(file = vmInstructionFile, newExtension = ".asm")

        //Write instead of append, to clear the file
        outputFile.writeBytes("""
           @256
           D=A
           @SP
           M=D
           
           ${CallInstruction("Sys.init", 0).toAsmInstructions()}
        """.trimIndent().toByteArray())

        when(vmInstructionFile.isFile) {
            true -> outputFile.appendBytes(writeToString(vmInstructionFile).toByteArray())
            false -> vmInstructionFile.listFiles().forEach {
                outputFile.appendBytes("// File : ${it.name}\n".toByteArray())
                outputFile.appendBytes(writeToString(it).toByteArray())
            }
        }

        val trimmedAsmProgram = outputFile.readLines()
            .joinToString(separator = System.lineSeparator()) { it.trim() }
            .toByteArray()

        outputFile.writeBytes(trimmedAsmProgram)
        return outputFile
    }

    private fun writeToString(vmFile: File): String {
        return vmFile.readLines()
            .map { cleanupLine(it) }
            .filterNot { it.isEmpty() }
            .joinToString(System.lineSeparator()) { vmInstruction -> "// $vmInstruction${System.lineSeparator()}" + parser.toInstruction(vmInstruction).toAsmInstructions() }
    }

}