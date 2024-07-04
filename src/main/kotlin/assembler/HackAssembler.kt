package assembler

import utils.FileUtils
import utils.isCommentLine
import utils.sanitizeLine
import java.io.File

class HackAssembler(private val asmInstructionFile: File) {

    private val symbolTable = SymbolTable(asmInstructionFile.readLines())
    private val asmInstructionParser = AsmInstructionParser(symbolTable)

    fun writeToFile(): File {
        val outputFile = FileUtils.getFileWithNewExtension(file = asmInstructionFile, newExtension = ".hack")
        outputFile.writeBytes(writeToString().toByteArray())
        return outputFile
    }

    fun writeToString(): String {
        val asmInstructionsOnly = asmInstructionFile.readLines().asSequence()
            .map { sanitizeLine(it) }
            .filterNot { isCommentLine(it) }
            .filterNot { isLabelSymbolLine(it) }


        return asmInstructionsOnly
            .mapIndexed { index, asmLine -> parseInstruction(index, asmLine) }
            .joinToString(System.lineSeparator()) { it.toHackBinary() }
    }

    private fun parseInstruction(index: Int, asmLine: String): HackInstruction {
        val result = asmInstructionParser.toInstruction(asmLine)
        println("Parsed line #$index '$asmLine' to $result")
        return result
    }
}