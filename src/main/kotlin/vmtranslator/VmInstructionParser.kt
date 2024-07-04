package vmtranslator

class VmInstructionParser {

    fun toInstruction(vmInstruction: String): VmInstruction {
        return with(vmInstruction.trim()) {
            when {
                equals("add") -> AddInstruction()
                startsWith("push constant") -> parsePushConstantInstruction(this)
                else -> throw IllegalArgumentException("Unsupported Vm command '$vmInstruction'")
            }
        }
    }

    private fun parsePushConstantInstruction(vmInstruction: String): PushConstantInstruction {
        val constant = vmInstruction.substringAfter("push constant").trim().toShort()
        return PushConstantInstruction(constant)
    }
}