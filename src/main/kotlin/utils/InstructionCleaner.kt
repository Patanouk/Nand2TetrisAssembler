package utils

fun sanitizeLine(instruction: String) = removeComment(removeWhitespaces(instruction))

fun removeWhitespaces(instruction: String) = instruction.replace(" ", "")

fun removeComment(instruction: String) = instruction.substringBefore("//")

fun isCommentLine(instruction: String) = sanitizeLine(instruction).isEmpty()