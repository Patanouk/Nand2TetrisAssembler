import assembler.Assembler
import java.io.File

fun main(args : Array<String>) {
    val asmFile = File(args[0])
    val assembler = Assembler(asmFile).writeToFile()
}