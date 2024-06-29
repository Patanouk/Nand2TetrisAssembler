import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SymbolTableTest {

    @Test
    fun shouldParseSingleSymbolProperly() {
        //Given
        val asmLines = """
            @2
            D=D+1
            (LOOP)
            @1
        """.trimIndent().split(System.lineSeparator())

        //When
        val symbolTable = SymbolTable(asmLines)

        //Then
        assertEquals(2, symbolTable.getSymbolAddressOrAdd("LOOP"))
    }

    @Test
    fun shouldParseConsecutiveSymbolsProperly() {
        //Given
        val asmLines = """
            (LOOP1)
            (LOOP2)
            @1
        """.trimIndent().split(System.lineSeparator())

        //When
        val symbolTable = SymbolTable(asmLines)

        //Then
        assertEquals(0, symbolTable.getSymbolAddressOrAdd("LOOP1"))
        assertEquals(0, symbolTable.getSymbolAddressOrAdd("LOOP2"))
    }
}