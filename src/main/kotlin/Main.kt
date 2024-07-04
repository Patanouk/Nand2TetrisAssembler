import assembler.HackAssembler
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody
import java.io.File

fun main(args : Array<String>): Unit = mainBody {
    ArgParser(args).parseInto(::MyArgs).run {
        when(program) {
            ProgramType.Assembler -> HackAssembler(inputFile).writeToFile()
        }
    }
}
enum class ProgramType {
    Assembler,
}

class MyArgs(parser: ArgParser) {
    val inputFile by parser.storing(
        "-f", "--file",
        help = "input file to convert",) { File(this) }

    val program by parser.mapping(
        "--assembler" to ProgramType.Assembler,
        help = "type of program to run")
}