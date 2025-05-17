package assembler

import utils.cleanupLine

fun isLabelSymbolLine(asmLine: String) = cleanupLine(asmLine).startsWith('(')