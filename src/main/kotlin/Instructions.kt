data class AInstruction(val address: Short) {

    init {
        require(address >= 0) { "Negative address '$address' is not supported for A instructions" }
    }

    fun toHack(): String {
        return String.format("%16s", address.toString(2)).replace(' ', '0')
    }
}

data class CInstruction(val dest: String?, val comp: String, val jump: String?) {

    fun toHack(): String {
        val sortedDestination = dest?.toCharArray()?.apply { sort() }?.joinToString(separator = "")

        val destBinary = destinationTable[sortedDestination] ?: throw IllegalArgumentException("Destination '$dest' is not supported for C instruction")
        val comBinary = compTable[comp] ?: throw IllegalArgumentException("Computation '$comp' is not supported for C instruction")
        val jumpBinary = jumpTable[jump] ?: throw IllegalArgumentException("Jump '$jump' is not supported for C instruction")

        return "111$comBinary$destBinary$jumpBinary"
    }

    companion object {
        val destinationTable = mapOf(
            null to "000",
            "M" to "001",
            "D" to "010",
            "DM" to "011",
            "A" to "100",
            "AM" to "101",
            "AD" to "110",
            "ADM" to "111",
        )

        val jumpTable = mapOf(
            null to "000",
            "JGT" to "001",
            "JEQ" to "010",
            "JGE" to "011",
            "JLT" to "100",
            "JNE" to "101",
            "JLE" to "110",
            "JMP" to "111",
        )

        val compTable = mapOf(
            "0" to "0101010",
            "1" to "0111111",
            "-1" to "0111010",
            "D" to "0001100",
            "A" to "0110000",
            "!D" to "0001101",
            "!A" to "0110001",
            "-D" to "0001111",
            "-A" to "0110011",
            "D+1" to "0011111",
            "A+1" to "0110111",
            "D-1" to "0001110",
            "A-1" to "0110010",
            "D+A" to "0000010",
            "D-A" to "0010011",
            "A-D" to "0000111",
            "D&A" to "0000000",
            "D|A" to "0010101",
            "M" to "1110000",
            "!M" to "1110001",
            "-M" to "1110011",
            "M+1" to "1110111",
            "M-1" to "1110010",
            "D+M" to "1000010",
            "D-M" to "1010011",
            "M-D" to "1000111",
            "D&M" to "1000000",
            "D|M" to "1010101",
        )
    }
}