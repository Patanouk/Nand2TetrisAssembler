import java.io.File

class Assembler {

    companion object {
        fun parseToBinary(asmInstructionFile: File): String {
            return asmInstructionFile.readLines().asSequence()
                .filterNot { isCommentLine(it) }
                .filterNot { isLabelSymbolLine(it) }
                .map { sanitizeLine(it) }
                .map { InstructionParser.toInstruction(it) }
                .joinToString(System.lineSeparator()) { it.toHackBinary() }
        }

        private fun isCommentLine(asmLine: String): Boolean {
            val sanitizedAsmInstruction = asmLine.substringBefore("//").replace(" ", "")
            if (sanitizedAsmInstruction.isEmpty()) {
                println("Skipping comment line '$asmLine'")
                return true
            }

            return false;
        }

        private fun isLabelSymbolLine(asmLine: String): Boolean {
            return asmLine.trim().startsWith('(')
        }

        private fun sanitizeLine(asmLine: String): String {
            return asmLine.replace(" ", "")
        }
    }
}