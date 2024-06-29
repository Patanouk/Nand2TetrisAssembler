import java.io.File

class Assembler() {

    companion object {
        fun parseToBinary(asmInstructionFile: File): String {
            return asmInstructionFile.readLines()
                .mapNotNull { InstructionParser.toInstruction(it) }
                .joinToString(System.lineSeparator()) { it.toHackBinary() }
        }
    }
}