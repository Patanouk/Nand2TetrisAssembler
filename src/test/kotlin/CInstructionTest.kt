import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.Test

class CInstructionTest {

    @ParameterizedTest
    @MethodSource("aInstructionProvider")
    fun `should parse A instruction properly`(dest: String?, comp: String, jump: String?, expectedHackInstruction: String) {
        //Given
        val cInstruction = CInstruction(dest, comp, jump)

        //When
        val actuallHackInstruction = cInstruction.toHack()

        assertEquals(expectedHackInstruction, actuallHackInstruction)
    }

    @Test
    fun `should throw if destination is unknown`() {
        //Given
        val cInstruction = CInstruction(dest = "unknown", comp = "0", jump = "JGT")

        //When, Then
        val exception = assertThrows<IllegalArgumentException> { cInstruction.toHack() }
        assertEquals("Destination 'unknown' is not supported for C instruction", exception.message)
    }

    @Test
    fun `should throw if jump is unknown`() {
        //Given
        val cInstruction = CInstruction(dest = "M", comp = "0", jump = "unknown")

        //When, Then
        val exception = assertThrows<IllegalArgumentException> { cInstruction.toHack() }
        assertEquals("Jump 'unknown' is not supported for C instruction", exception.message)
    }

    @Test
    fun `should throw if computation is unknown`() {
        //Given
        val cInstruction = CInstruction(dest = "M", comp = "unknown", jump = "JGT")

        //When, Then
        val exception = assertThrows<IllegalArgumentException> { cInstruction.toHack() }
        assertEquals("Computation 'unknown' is not supported for C instruction", exception.message)
    }

    companion object {
        @JvmStatic
        fun aInstructionProvider(): Stream<Arguments> = Stream.of(
            arguments(null, "0", null, "1110101010000000"),
            arguments("D", "D+1", "JLE", "1110011111010110"),
            arguments("DM", "D+1", "JLE", "1110011111011110"),
            arguments("MD", "D+1", "JLE", "1110011111011110"),
            arguments("ADM", "D+1", "JLE", "1110011111111110"),
            arguments("AMD", "D+1", "JLE", "1110011111111110"),
        )
    }
}