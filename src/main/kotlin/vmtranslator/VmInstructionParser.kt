package vmtranslator

class VmInstructionParser {

    fun toInstruction(vmInstruction: String): VmInstruction {
        return with(vmInstruction.trim()) {
            when {
                equals("add") -> AddInstruction
                equals("sub") -> SubInstruction
                equals("neg") -> NegInstruction
                equals("eq") -> EqInstruction
                equals("gt") -> GtInstruction
                equals("lt") -> LtInstruction
                equals("and") -> AndInstruction
                equals("or") -> OrInstruction
                equals("not") -> NotInstruction
                startsWith("push constant") -> parsePushConstantInstruction(this)
                startsWith("pop") -> parsePopInstruction(this)
                else -> throw IllegalArgumentException("Unsupported Vm command '$vmInstruction'")
            }
        }
    }

    private fun parsePushConstantInstruction(vmInstruction: String): PushConstantInstruction {
        val constant = vmInstruction.substringAfter("push constant").trim().toShort()
        return PushConstantInstruction(constant)
    }

    private fun parsePopInstruction(vmInstruction: String): VmInstruction {
        val splitPopInstruction = vmInstruction.trim().split(' ')
        val segment = splitPopInstruction[1]
        val address = splitPopInstruction[2].toShort()

        return when (segment) {
            "local",
            "argument",
            "this",
            "that", -> PopSegmentInstruction(segment, address)
            else -> throw IllegalArgumentException("Unsupported segmengt '$segment' in instruction '$vmInstruction'")
        }
    }
}