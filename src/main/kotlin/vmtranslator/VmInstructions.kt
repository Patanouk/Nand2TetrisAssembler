package vmtranslator

interface VmInstruction {
    fun toAsmInstructions(): String
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
    override fun toAsmInstructions(): String {
        return """
            @SP
            M=M-1
            A=M
            D=M
            A=A-1
            D=D-M
            @EQUAL
            D;JEQ
            @NOTEQUAL
            D;JNE
            (EQUAL)
                @0
                D=A
                @LOAD
                0;JMP
            (NOTEQUAL)
                @1
                D=A
            (LOAD)
                @SP
                A=M-1
                M=D
        """.trimIndent()
    }

}

class PushConstantInstruction(private val constant: Short): VmInstruction {
    override fun toAsmInstructions(): String {
        return """
            @$constant
            D=A
            @SP
            A=M
            M=D
            @SP
            M=M+1
        """.trimIndent()
    }
}