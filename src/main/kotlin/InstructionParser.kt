class InstructionParser {
    companion object {
        fun toInstruction(asmInstruction: String): HackInstruction {
            return when(asmInstruction.first()) {
                '@' -> parseAInstruction(asmInstruction)
                else -> parseCInstruction(asmInstruction)
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