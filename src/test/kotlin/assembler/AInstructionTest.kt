package assembler

import assembler.AInstruction
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals

class AInstructionTest {

    @ParameterizedTest
    @MethodSource("aInstructionProvider")
    fun `should parse A instruction properly`(address: Short, expectedHackInstruction: String) {
        //Given
        val aInstruction = AInstruction(address)

        //When
        val actuallHackInstruction = aInstruction.toHackBinary()

        assertEquals(expectedHackInstruction, actuallHackInstruction)
    }

    @Test
    fun `should throw if address is too big`() {
        //Given
        val address = (-1).toShort()

        //When, Then
        val exception = assertThrows<IllegalArgumentException> { AInstruction(address) }
        assertEquals(exception.message, "Negative address '-1' is not supported for A instructions")
    }

    companion object {
        @JvmStatic
        fun aInstructionProvider(): Stream<Arguments> = Stream.of(
            arguments(17.toShort(), "0000000000010001"),
            arguments(0.toShort(), "0000000000000000"),
            arguments(1.toShort(), "0000000000000001"),
        )
    }
}