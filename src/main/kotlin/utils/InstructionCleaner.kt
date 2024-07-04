package utils

fun sanitizeLine(asmLine: String) = asmLine.replace(" ", "").substringBefore("//")

fun isCommentLine(asmLine: String) = sanitizeLine(asmLine).isEmpty()