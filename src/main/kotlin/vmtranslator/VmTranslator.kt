package vmtranslator

import utils.FileUtils
import utils.cleanupLine
import java.io.File

class VmTranslator(private val vmInstructionFile: File) {

    fun writeToFile(): File {
        val outputFile = FileUtils.getFileWithNewExtension(file = vmInstructionFile, newExtension = ".asm")
        outputFile.writeBytes("".toByteArray()) //Clear file

        outputFile.appendBytes(INIT_INSTRUCTION)

        when(vmInstructionFile.isFile) {
            true -> outputFile.appendBytes(writeToString(vmInstructionFile).toByteArray())
            false -> vmInstructionFile.listFiles().forEach {
                outputFile.appendBytes(writeFileName(it))
                outputFile.appendBytes(writeToString(it).toByteArray())
            }
        }

        val trimmedAsmProgram = outputFile.readLines()
            .joinToString(separator = System.lineSeparator()) { it.trim() }
            .toByteArray()

        outputFile.writeBytes(trimmedAsmProgram)
        return outputFile
    }

    private fun writeFileName(it: File) =
        """
            ${System.lineSeparator()}
            // File : ${it.name} //
            ${System.lineSeparator()}
        """.trimIndent().toByteArray()

    private fun writeToString(vmFile: File): String {
        val parser = VmInstructionParser(vmFile.nameWithoutExtension)

        return vmFile.readLines()
            .map { cleanupLine(it) }
            .filterNot { it.isEmpty() }
            .joinToString(System.lineSeparator()) { vmInstruction -> "// $vmInstruction${System.lineSeparator()}" + parser.toInstruction(vmInstruction).toAsmInstructions() }
    }

    companion object {
        private val INIT_INSTRUCTION = """
           //INIT CODE
           @256
           D=A
           @SP
           M=D

           ${CallInstruction("Sys.init", 0).toAsmInstructions()}
        """.trimIndent().toByteArray()
    }

}
