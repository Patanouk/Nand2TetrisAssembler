import java.io.File

class Assembler(private val asmInstructionFile: File) {

    private val symbolTable = SymbolTable(asmInstructionFile.readLines());

    fun parseToBinary(): String {

        return asmInstructionFile.readLines().asSequence()
            .map { sanitizeLine(it) }
            .filterNot { isCommentLine(it) }
            .filterNot { isLabelSymbolLine(it) }
            .map { InstructionParser.toInstruction(it) }
            .joinToString(System.lineSeparator()) { it.toHackBinary() }
    }
}