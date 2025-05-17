package utils

fun cleanupLine(instruction: String) = instruction
    .substringBefore("//")
    .trim()
    .replace("\t", "")