data class AInstruction(val address: Short) {

    init {
        require(address >= 0) { "Negative address '$address' is not supported for A instructions" }
    }

    fun toHack(): String {
        return String.format("%16s", address.toString(2)).replace(' ', '0');
    }
}