package vmtranslator

class VmInstructionParser(private val fileName: String) {

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
                startsWith("pop") -> parsePopInstruction(this)
                startsWith("push") -> parsePushInstruction(this)
                startsWith("label") -> parseLabelInstruction(this)
                else -> throw IllegalArgumentException("Unsupported Vm command '$vmInstruction'")
            }
        }
    }

    private fun parsePopInstruction(vmInstruction: String): VmInstruction {
        val (segment, address) = splitInstruction(vmInstruction)

        return when (segment) {
            "local",
            "argument",
            "this",
            "that", -> PopSegmentInstruction(segment, address)
            "static" -> PopStaticInstruction(address, fileName)
            "temp" -> PopTempInstruction(address)
            "pointer" -> PopPointerInstruction(address)
            else -> throw IllegalArgumentException("Unsupported segmengt '$segment' in instruction '$vmInstruction'")
        }
    }

    private fun parsePushInstruction(vmInstruction: String): VmInstruction {
        val (segment, address) = splitInstruction(vmInstruction)

        return when (segment) {
            "local",
            "argument",
            "this",
            "that", -> PushSegmentInstruction(segment, address)
            "constant" -> PushConstantInstruction(address)
            "static" -> PushStaticInstruction(address, fileName)
            "temp" -> PushTempInstruction(address)
            "pointer" -> PushPointerInstruction(address)
            else -> throw IllegalArgumentException("Unsupported segmengt '$segment' in instruction '$vmInstruction'")
        }
    }

    private fun splitInstruction(vmInstruction: String): Pair<String, Short> {
        val splitPopInstruction = vmInstruction.trim().split(' ')
        val segment = splitPopInstruction[1]
        val address = splitPopInstruction[2].toShort()

        return Pair(segment, address)
    }

    private fun parseLabelInstruction(vmInstruction: String): VmInstruction {
        val labelName = vmInstruction.split(' ')[1]
        return LabelInstruction(labelName)
    }
}