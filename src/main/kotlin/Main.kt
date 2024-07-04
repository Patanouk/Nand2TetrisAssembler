import assembler.HackAssembler
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody
import vmtranslator.VmTranslator
import java.io.File

fun main(args : Array<String>): Unit = mainBody {
    ArgParser(args).parseInto(::MyArgs).run {
        when(program) {
            ProgramType.Assembler -> HackAssembler(inputFile).writeToFile()
            ProgramType.VmTranslator -> VmTranslator(inputFile).writeToFile()
        }
    }
}
enum class ProgramType {
    Assembler, VmTranslator,
}

class MyArgs(parser: ArgParser) {
    val inputFile by parser.storing(
        "-f", "--file",
        help = "input file to convert",) { File(this) }

    val program by parser.mapping(
        "--assembler" to ProgramType.Assembler,
        "--vm-translator" to ProgramType.VmTranslator,
        help = "type of program to run")
}