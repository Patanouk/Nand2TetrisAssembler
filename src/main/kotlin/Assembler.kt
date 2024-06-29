import java.io.File

class Assembler(private val asmInstructionFile: File) {

    private val symbolTable = SymbolTable(asmInstructionFile.readLines())
    private val instructionParser = InstructionParser(symbolTable)

    fun parseToBinary(): String {
        val asmInstructionsOnly = asmInstructionFile.readLines().asSequence()
            .map { sanitizeLine(it) }
            .filterNot { isCommentLine(it) }
            .filterNot { isLabelSymbolLine(it) }


        return asmInstructionsOnly
            .mapIndexed { index, asmLine -> parseInstruction(index, asmLine) }
            .joinToString(System.lineSeparator()) { it.toHackBinary() }
    }

    private fun parseInstruction(index: Int, asmLine: String): HackInstruction {
        val result = instructionParser.toInstruction(asmLine)
        println("Parsed line #$index '$asmLine' to $result")
        return result
    }
}