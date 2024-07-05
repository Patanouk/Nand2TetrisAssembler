package vmtranslator

interface VmInstruction {
    fun toAsmInstructions(): String

    fun conditionInstruction(condition: String) = """
            @SP
            AM=M-1
            D=M
            A=A-1
            D=D-M
            @CONDITION
            D;$condition
            @NOTCONDITION
            0;JMP
            (CONDITION)
                @0
                D=A
                @LOAD
                0;JMP
            (NOTCONDITION)
                @1
                D=A
            (LOAD)
                @SP
                A=M-1
                M=D
        """.trimIndent()

    fun reducerInstruction(reducerOperand: String) = """
            @SP
            AM=M-1
            D=M
            A=A-1
            M=D${reducerOperand}M
        """.trimIndent()

    fun operandInstruction(operand: String) = """
            @SP
            A=M-1
            M=${operand}M
        """.trimIndent()
}

object AddInstruction: VmInstruction {
    override fun toAsmInstructions() = reducerInstruction("+")
}

object SubInstruction: VmInstruction {
    override fun toAsmInstructions() = reducerInstruction("-")
}

object NegInstruction: VmInstruction {
    override fun toAsmInstructions() = operandInstruction("-")
}

object EqInstruction: VmInstruction {
    override fun toAsmInstructions() = conditionInstruction("JEQ")
}

object GtInstruction: VmInstruction {
    override fun toAsmInstructions() = conditionInstruction("JGT")
}

object LtInstruction: VmInstruction {
    override fun toAsmInstructions() = conditionInstruction("JLT")
}

object AndInstruction: VmInstruction {
    override fun toAsmInstructions() = reducerInstruction("&")
}

object OrInstruction: VmInstruction {
    override fun toAsmInstructions() = reducerInstruction("|")
}

object NotInstruction: VmInstruction {
    override fun toAsmInstructions() = operandInstruction("!")
}

class PushConstantInstruction(private val constant: Short): VmInstruction {
    override fun toAsmInstructions(): String {
        return """
            @$constant
            D=A
            @SP
            M=M+1
            A=M-1
            M=D
        """.trimIndent()
    }
}