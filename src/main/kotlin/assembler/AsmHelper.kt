package assembler

import utils.sanitizeLine

fun isLabelSymbolLine(asmLine: String) = sanitizeLine(asmLine).startsWith('(')