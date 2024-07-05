package vmtranslator

import vmtranslator.VmInstruction.Companion.FALSE
import vmtranslator.VmInstruction.Companion.TRUE

interface VmInstruction {
    fun toAsmInstructions(): String

    companion object {
        const val FALSE = "0"
        const val TRUE = "-1"
    }
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
            D=M-D
            @TRUE_CONDITION_$counter
            D;${jumpCondition()}
            D=$FALSE
            @LOAD_$counter
            0;JMP
            (TRUE_CONDITION_$counter)
                D=$TRUE
                @LOAD_$counter
                0;JMP
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
            M=M${reducerOperand()}D
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