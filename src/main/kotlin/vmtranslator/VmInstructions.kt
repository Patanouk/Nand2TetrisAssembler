package vmtranslator

interface VmInstruction {
    fun toAsmInstructions(): String
}

abstract class VmConditionalInstruction: VmInstruction {
    abstract fun jumpCondition(): String

    final override fun toAsmInstructions(): String {
        counter += 1

        return """
            @SP
            AM=M-1
            D=M
            A=A-1
            D=D-M
            @CONDITION_$counter
            D;${jumpCondition()}
            @NOTCONDITION_$counter
            0;JMP
            (CONDITION_$counter)
                D=0
                @LOAD_$counter
                0;JMP
            (NOTCONDITION_$counter)
                D=-1
            (LOAD_$counter)
                @SP
                A=M-1
                M=D
        """.trimIndent()
    }

    companion object {
        private var counter = 0
    }
}

abstract class VmReducerInstruction: VmInstruction {
    abstract fun reducerOperand(): String

    final override fun toAsmInstructions() = """
            @SP
            AM=M-1
            D=M
            A=A-1
            M=D${reducerOperand()}M
        """.trimIndent()
}

abstract class VmOperandInstruction: VmInstruction {
    abstract fun operand(): String

    final override fun toAsmInstructions() = """
            @SP
            A=M-1
            M=${operand()}M
        """.trimIndent()
}

object AddInstruction: VmReducerInstruction() {
    override fun reducerOperand() = "+"
}

object SubInstruction: VmReducerInstruction() {
    override fun reducerOperand() = "-"
}

object AndInstruction: VmReducerInstruction() {
    override fun reducerOperand() = "&"
}

object OrInstruction: VmReducerInstruction() {
    override fun reducerOperand() = "|"
}

object EqInstruction: VmConditionalInstruction() {
    override fun jumpCondition() = "JEQ"
}

object GtInstruction: VmConditionalInstruction() {
    override fun jumpCondition() = "JGT"
}

object LtInstruction: VmConditionalInstruction() {
    override fun jumpCondition() = "JLT"
}

object NegInstruction: VmOperandInstruction() {
    override fun operand() = "-"
}

object NotInstruction: VmOperandInstruction() {
    override fun operand() = "!"
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