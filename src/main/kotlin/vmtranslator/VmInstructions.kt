package vmtranslator

interface VmInstruction {
    fun toAsmInstructions(): String

    fun conditionInstruction(condition: String) = """
            @SP
            M=M-1
            A=M
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

    fun booleanInstruction(booleanInstruction: String) = """
            @SP
            M=M-1
            A=M
            D=M
            A=A-1
            M=D${booleanInstruction}M
        """.trimIndent()
}

object AddInstruction: VmInstruction {
    override fun toAsmInstructions(): String {
        return """
            @SP
            M=M-1
            A=M
            D=M
            A=A-1
            M=D+M
        """.trimIndent()
    }
}

object SubInstruction: VmInstruction {
    override fun toAsmInstructions(): String {
        return """
            @SP
            M=M-1
            A=M
            D=M
            A=A-1
            M=D-M
        """.trimIndent()
    }
}

object NegInstruction: VmInstruction {
    override fun toAsmInstructions(): String {
        return """
            @SP
            A=M-1
            M=-M
        """.trimIndent()
    }
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
    override fun toAsmInstructions() = booleanInstruction("&")
}

object OrInstruction: VmInstruction {
    override fun toAsmInstructions() = booleanInstruction("|")
}

object NotInstruction: VmInstruction {
    override fun toAsmInstructions(): String {
        return """
            @SP
            A=M-1
            M=!M
        """.trimIndent()
    }
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