import java.io.File

class Assembler(private val asmInstructionFile: File) {

    private val symbolTable = SymbolTable(asmInstructionFile.readLines())
    private val instructionParser = InstructionParser(symbolTable)

    fun parseToBinary(): String {

        return asmInstructionFile.readLines().asSequence()
            .map { sanitizeLine(it) }
            .filterNot { isCommentLine(it) }
            .filterNot { isLabelSymbolLine(it) }
            .map { instructionParser.toInstruction(it) }
            .joinToString(System.lineSeparator()) { it.toHackBinary() }
    }
}