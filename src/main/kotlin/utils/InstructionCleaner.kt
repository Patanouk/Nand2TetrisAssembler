package utils

fun cleanupLine(instruction: String) = instruction
    .substringBefore("//")
    .replace(" ", "")
    .replace("\t", "")