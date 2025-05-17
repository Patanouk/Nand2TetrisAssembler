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

abstract class VmConditionalInstruction : VmInstruction {
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
            D=$FALSE // If no jump, we know the result is false
            @LOAD_$counter
            0;JMP
            (TRUE_CONDITION_$counter)
            D=$TRUE
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

abstract class VmReducerInstruction : VmInstruction {
    abstract fun reducerOperand(): String

    final override fun toAsmInstructions() = """
            @SP
            AM=M-1
            D=M
            A=A-1
            M=M${reducerOperand()}D
        """.trimIndent()
}

abstract class VmOperandInstruction : VmInstruction {
    abstract fun operand(): String

    final override fun toAsmInstructions() = """
            @SP
            A=M-1
            M=${operand()}M
        """.trimIndent()
}

object AddInstruction : VmReducerInstruction() {
    override fun reducerOperand() = "+"
}

object SubInstruction : VmReducerInstruction() {
    override fun reducerOperand() = "-"
}

object AndInstruction : VmReducerInstruction() {
    override fun reducerOperand() = "&"
}

object OrInstruction : VmReducerInstruction() {
    override fun reducerOperand() = "|"
}

object EqInstruction : VmConditionalInstruction() {
    override fun jumpCondition() = "JEQ"
}

object GtInstruction : VmConditionalInstruction() {
    override fun jumpCondition() = "JGT"
}

object LtInstruction : VmConditionalInstruction() {
    override fun jumpCondition() = "JLT"
}

object NegInstruction : VmOperandInstruction() {
    override fun operand() = "-"
}

object NotInstruction : VmOperandInstruction() {
    override fun operand() = "!"
}

class PopSegmentInstruction(private val segment: String, private val address: Short) : VmInstruction {
    override fun toAsmInstructions() = """
        @$address
        D=A
        @${segmentToPointer[segment]}
        D=D+M
        @SP
        AM=M-1
        D=D+M
        A=D-M
        M=D-A
    """.trimIndent()

    companion object {
        private val segmentToPointer = mapOf(
            "local" to "LCL",
            "argument" to "ARG",
            "this" to "THIS",
            "that" to "THAT",
        )
    }
}

class PushSegmentInstruction(private val segment: String, private val address: Short) : VmInstruction {
    override fun toAsmInstructions() = """
        @$address
        D=A
        @${segmentToPointer[segment]}
        A=D+M
        D=M
        ${pushDRegisterToStack()}
    """.trimIndent()

    companion object {
        private val segmentToPointer = mapOf(
            "local" to "LCL",
            "argument" to "ARG",
            "this" to "THIS",
            "that" to "THAT",
        )
    }
}

class PushConstantInstruction(private val constant: Short) : VmInstruction {
    override fun toAsmInstructions() = """
            @$constant
            D=A
            ${pushDRegisterToStack()}
        """.trimIndent()
}

class PushStaticInstruction(private val address: Short, private val fileName: String) : VmInstruction {
    override fun toAsmInstructions() = """
        @$fileName.$address
        D=M
        ${pushDRegisterToStack()}
    """.trimIndent()
}

class PopStaticInstruction(private val address: Short, private val fileName: String) : VmInstruction {
    override fun toAsmInstructions() = """
        @SP
        AM=M-1
        D=M
        @$fileName.$address
        M=D
    """.trimIndent()
}

class PopTempInstruction(private val address: Short) : VmInstruction {
    init {
        require(address in 0..7) { "Address '$address' for temp instruction should be between 0 and 7" }
    }

    override fun toAsmInstructions() = """
        @${address + 5}
        D=A
        @SP
        AM=M-1
        D=D+M
        A=D-M
        M=D-A
    """.trimIndent()
}

class PushTempInstruction(private val address: Short) : VmInstruction {
    init {
        require(address in 0..7) { "Address '$address' for temp instruction should be between 0 and 7" }
    }

    override fun toAsmInstructions() = """
        @${address + 5}
        D=M
        ${pushDRegisterToStack()}
    """.trimIndent()

}

class PushPointerInstruction(private val pointerAddress: Short) : VmInstruction {
    init {
        require(pointerAddress in 0..1) { "Pointer '$pointerAddress' should be either 0 or 1" }
    }

    override fun toAsmInstructions() = """
        @${pointerMap[pointerAddress]}
        D=M
        ${pushDRegisterToStack()}
    """.trimIndent()

    companion object {
        private val pointerMap = mapOf(
            0.toShort() to "THIS",
            1.toShort() to "THAT",
        )
    }
}

class PopPointerInstruction(private val pointerAddress: Short) : VmInstruction {
    init {
        require(pointerAddress in 0..1) { "Pointer '$pointerAddress' should be either 0 or 1" }
    }

    override fun toAsmInstructions() = """
        @SP
        AM=M-1
        D=M
        @${pointerMap[pointerAddress]}
        M=D
    """.trimIndent()

    companion object {
        private val pointerMap = mapOf(
            0.toShort() to "THIS",
            1.toShort() to "THAT",
        )
    }
}

class GotoLabelInstruction(private val labelName: String) : VmInstruction {
    override fun toAsmInstructions() = """
        @$labelName
        0;JMP
    """.trimIndent()
}

class IfGotoLabelInstruction(private val labelName: String) : VmInstruction {
    override fun toAsmInstructions() = """
        @SP
        AM=M-1
        D=M
        @$labelName
        D;JNE
    """.trimIndent()

}

class LabelInstruction(private val labelName: String) : VmInstruction {
    override fun toAsmInstructions() = "($labelName)"
}

/**
 * This will
 * 1. Push the (returned_address) label of the function
 * 2. Push LCL, ARG, THIS, THAT in that order
 * 3. Set ARG to SP - 5 (number of previously pushed vars) - nArgs
 * 4. Set LCL to SP
 * 5. Create the (returned_address)
 */
class CallInstruction(private val functionName: String, private val nArgs: Int) : VmInstruction {

    override fun toAsmInstructions(): String {
        RETURN_NUMBER += 1

        return """
            @RET_${functionName}_${RETURN_NUMBER}
            D=A
            ${pushDRegisterToStack()}
            
            @LCL
            D=M
            ${pushDRegisterToStack()}
            
            @ARG
            D=M
            ${pushDRegisterToStack()}
            
            @THIS
            D=M
            ${pushDRegisterToStack()}
            
            @THAT
            D=M
            ${pushDRegisterToStack()}
            
            @${nArgs + 5}
            D=A
            @SP
            D=A-D
            @ARG
            M=D
            
            @SP
            D=A
            @LCL
            M=D
            
            @$functionName
            0;JMP
            
            (RET_${functionName}_${RETURN_NUMBER})
        """.trimIndent()
    }

    companion object {
        private var RETURN_NUMBER = 0
    }
}

/**
 * This will
 * 1. Generate a label name with the function name
 * 2. Push as many zero to the stack as nVars
 */
class FunctionInstruction(private val functionName: String, private val nVars: Int) : VmInstruction {
    override fun toAsmInstructions(): String {
        return """
            ($functionName)
            @$nVars
            D=A
            
            (INIT_ARGS_${functionName}_${RETURN_NUMBER})
            @AFTER_INIT_ARGS_${functionName}_${RETURN_NUMBER}
            D;JEQ
            
            @SP
            M=M+1
            A=M-1
            M=0
            D=D-1
            @INIT_ARGS_${functionName}_${RETURN_NUMBER}
            D;JMP
            (AFTER_INIT_ARGS_${functionName}_${RETURN_NUMBER})
            
        """.trimIndent()
        }

    companion object {
        private var RETURN_NUMBER = 0
    }
}

/**
 * This will
 * 1. Save LCL in a variable (endFrame)
 * 2. Save *(endFrame - 5) as retAddr
 * 3. Set *ARG = pop() stack
 * 4. SP = ARG + 1
 * 5. THAT = *(endFrame – 1)
 * 6. THIS = *(endFrame – 2)
 * 7. ARG = *(endFrame – 3)
 * 8. LCL = *(endFrame – 4)
 * 9. goto retAddr
 */
object ReturnInstruction: VmInstruction {
    override fun toAsmInstructions() = """
        @LCL
        D=M
        @R13
        M=D
        
        @5
        A=D-A
        D=M
        @R14
        M=D
        
        @SP
        AM=M-1
        D=M
        @ARG
        A=M
        M=D
        
        @ARG
        D=M
        @SP
        M=D+1
        
        @R13
        AM=M-1
        D=M
        @THAT
        M=D
        
        @R13
        AM=M-1
        D=M
        @THIS
        M=D
        
        @R13
        AM=M-1
        D=M
        @ARG
        M=D
        
        @R13
        AM=M-1
        D=M
        @LCL
        M=D
        
        @R14
        A=M
        0;JMP
        
    """.trimIndent()

}

private fun pushDRegisterToStack() = """
        @SP
        M=M+1
        A=M-1
        M=D
""".trimIndent()