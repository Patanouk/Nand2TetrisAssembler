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
            @SP
            M=M-1
            A=M
            M=D+M
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