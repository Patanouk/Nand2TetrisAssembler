package assembler

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.io.File
import java.util.stream.Stream

class HackAssemblerTest {

    @ParameterizedTest
    @MethodSource("hackNoSymbolProvider")
    fun testAssemblerNoSymbol(testFilePath: String) {
        //Given
        val asmFile = File(HackAssemblerTest::class.java.getResource("$testFilePath.asm").file)
        val expectedHackInstructions = File(HackAssemblerTest::class.java.getResource("$testFilePath.hack").file)
            .readLines()
            .filterNot { it.isEmpty() }
            .joinToString(System.lineSeparator())

        //When
        val actualAsmInstruction = HackAssembler(asmFile).writeToString()

        //Then
        assertEquals(expectedHackInstructions, actualAsmInstruction)
    }

    @ParameterizedTest
    @MethodSource("hackWithymbolProvider")
    fun testAssemblerWithSymbol(testFilePath: String) {
        //Given
        val asmFile = File(HackAssemblerTest::class.java.getResource("$testFilePath.asm").file)
        val expectedHackInstructions = File(HackAssemblerTest::class.java.getResource("$testFilePath.hack").file)
            .readLines()
            .filterNot { it.isEmpty() }
            .joinToString(System.lineSeparator())

        //When
        val actualAsmInstruction = HackAssembler(asmFile).writeToString()

        //Then
        assertEquals(expectedHackInstructions, actualAsmInstruction)
    }

    companion object {
        @JvmStatic
        fun hackNoSymbolProvider(): Stream<Arguments> = Stream.of(
            Arguments.arguments("/assembler/add/Add"),
            Arguments.arguments("/assembler/max/MaxL"),
            Arguments.arguments("/assembler/rect/RectL"),
            Arguments.arguments("/assembler/pong/PongL"),
        )

        @JvmStatic
        fun hackWithymbolProvider(): Stream<Arguments> = Stream.of(
            Arguments.arguments("/assembler/max/Max"),
            Arguments.arguments("/assembler/rect/Rect"),
            Arguments.arguments("/assembler/pong/Pong"),
        )
    }
}