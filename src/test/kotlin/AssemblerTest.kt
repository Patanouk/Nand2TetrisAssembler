import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.io.File
import java.util.stream.Stream

class AssemblerTest {

    @ParameterizedTest
    @MethodSource("hackNoSymbolProvider")
    fun testAssemblerNoSymbol(testFilePath: String) {
        //Given
        val asmFile = File(AssemblerTest::class.java.getResource("$testFilePath.asm").file)
        val expectedHackInstructions = File(AssemblerTest::class.java.getResource("$testFilePath.hack").file)
            .readLines()
            .filterNot { it.isEmpty() }
            .joinToString(System.lineSeparator())

        //When
        val actualAsmInstruction = Assembler(asmFile).parseToBinary()

        //Then
        assertEquals(expectedHackInstructions, actualAsmInstruction)
    }

    @ParameterizedTest
    @MethodSource("hackWithymbolProvider")
    fun testAssemblerWithSymbol(testFilePath: String) {
        //Given
        val asmFile = File(AssemblerTest::class.java.getResource("$testFilePath.asm").file)
        val expectedHackInstructions = File(AssemblerTest::class.java.getResource("$testFilePath.hack").file)
            .readLines()
            .filterNot { it.isEmpty() }
            .joinToString(System.lineSeparator())

        //When
        val actualAsmInstruction = Assembler(asmFile).parseToBinary()

        //Then
        assertEquals(expectedHackInstructions, actualAsmInstruction)
    }

    companion object {
        @JvmStatic
        fun hackNoSymbolProvider(): Stream<Arguments> = Stream.of(
            Arguments.arguments("add/Add"),
            Arguments.arguments("max/MaxL"),
            Arguments.arguments("rect/RectL"),
            Arguments.arguments("pong/PongL"),
        )

        @JvmStatic
        fun hackWithymbolProvider(): Stream<Arguments> = Stream.of(
            Arguments.arguments("max/Max"),
            Arguments.arguments("rect/Rect"),
            Arguments.arguments("pong/Pong"),
        )
    }
}