class InstructionParser {
    companion object {
        fun toInstruction(asmInstruction: String): HackInstruction? {
            val sanitizedAsmInstruction = asmInstruction.substringBefore("//").replace(" ", "")
            if (sanitizedAsmInstruction.isEmpty()) {
                println("Skipping comment line '$asmInstruction'")
                return null
            }

            return when(sanitizedAsmInstruction.first()) {
                '@' -> parseAInstruction(sanitizedAsmInstruction)
                else -> parseCInstruction(sanitizedAsmInstruction)
            }
        }

        private fun parseCInstruction(asmInstruction: String): HackInstruction {
            val dest = asmInstruction.substringBefore('=', "").ifEmpty { null }
            val jump = asmInstruction.substringAfter(';', "").ifEmpty { null }
            val comp = asmInstruction.substringAfter('=').substringBefore(';')

            return CInstruction(dest, comp, jump)
        }

        private fun parseAInstruction(asmInstruction: String): HackInstruction {
            val address = asmInstruction.substring(1).toShort()
            return AInstruction(address)
        }

    }
}