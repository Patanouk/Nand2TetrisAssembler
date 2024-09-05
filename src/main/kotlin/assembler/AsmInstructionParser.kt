package assembler

class AsmInstructionParser(private val symbolTable: SymbolTable) {

    fun toInstruction(asmInstruction: String): AsmInstruction {
        return when (asmInstruction.first()) {
            '@' -> parseAInstruction(asmInstruction)
            else -> parseCInstruction(asmInstruction)
        }
    }

    private fun parseCInstruction(asmInstruction: String): CInstruction {
        val dest = asmInstruction.substringBefore('=', "").ifEmpty { null }
        val jump = asmInstruction.substringAfter(';', "").ifEmpty { null }
        val comp = asmInstruction.substringAfter('=').substringBefore(';')

        return CInstruction(dest, comp, jump)
    }

    private fun parseAInstruction(asmInstruction: String): AInstruction {
        val addressString = asmInstruction.substring(1)
        val address = addressString.toShortOrNull() ?: symbolTable.getSymbolAddressOrAdd(addressString)

        return AInstruction(address)
    }
}