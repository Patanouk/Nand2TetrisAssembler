package assembler

fun isCommentLine(asmLine: String) = sanitizeLine(asmLine).isEmpty()

fun isLabelSymbolLine(asmLine: String) = sanitizeLine(asmLine).startsWith('(')

fun sanitizeLine(asmLine: String) = asmLine.replace(" ", "").substringBefore("//")