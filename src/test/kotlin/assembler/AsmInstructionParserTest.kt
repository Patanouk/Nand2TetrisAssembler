package assembler

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.*
import java.util.stream.Stream

class AsmInstructionParserTest {
    private val asmInstructionParser = AsmInstructionParser(SymbolTable(Collections.emptyList()))

    @ParameterizedTest
    @MethodSource("aInstructionProvider")
    fun testParseAInstruction(aInstruction: String, expectedAInstruction: AInstruction) {
        //When
        val actualAInstruction = asmInstructionParser.toInstruction(aInstruction)

        //Then
        assertEquals(expectedAInstruction, actualAInstruction)
    }

    @ParameterizedTest
    @MethodSource("cInstructionProvider")
    fun testParseCInstruction(cInstruction: String, expectedCInstruction: CInstruction) {
        //When
        val actualCInstruction = asmInstructionParser.toInstruction(cInstruction)

        //Then
        assertEquals(expectedCInstruction, actualCInstruction)
    }

    companion object {
        @JvmStatic
        fun aInstructionProvider(): Stream<Arguments> = Stream.of(
            Arguments.arguments("@1", AInstruction(1)),
            Arguments.arguments("@12", AInstruction(12)),
            Arguments.arguments("@12", AInstruction(12)),
            Arguments.arguments("@123", AInstruction(123)),
            Arguments.arguments("@16000", AInstruction(16000)),
        )

        @JvmStatic
        fun cInstructionProvider(): Stream<Arguments> = Stream.of(
            Arguments.arguments("D=D+1;JLE", CInstruction("D", "D+1", "JLE")),
            Arguments.arguments("D+1", CInstruction(null, "D+1", null)),
            Arguments.arguments("D+1;JGT", CInstruction(null, "D+1", "JGT")),
            Arguments.arguments("D=D+1", CInstruction("D", "D+1", null)),
        )
    }
}