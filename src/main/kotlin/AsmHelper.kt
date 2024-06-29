fun isCommentLine(asmLine: String): Boolean {
    val sanitizedAsmInstruction = asmLine.substringBefore("//").replace(" ", "")
    if (sanitizedAsmInstruction.isEmpty()) {
        println("Skipping comment line '$asmLine'")
        return true
    }

    return false
}

fun isLabelSymbolLine(asmLine: String): Boolean {
    return asmLine.trim().startsWith('(')
}

fun sanitizeLine(asmLine: String): String {
    return asmLine.replace(" ", "")
}