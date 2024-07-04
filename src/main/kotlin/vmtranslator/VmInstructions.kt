package vmtranslator

interface VmInstruction {
    fun toAsmInstructions(): List<String>
}

class AddInstruction: VmInstruction {
    override fun toAsmInstructions(): List<String> {
        return """
            @SP
            M=M-1
            A=M
            D=M
            @SP
            M=M-1
            A=M
            M=D+M
        """.trimIndent().split("\n")
    }
}

class PushConstantInstruction(private val constant: Short): VmInstruction {
    override fun toAsmInstructions(): List<String> {
        return """
            @$constant
            D=A
            @SP
            A=M
            M=D
            @SP
            M=M+1
        """.trimIndent().split("\n")
    }

}