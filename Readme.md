This is a project for the software part of nand2Tetris  
So far, this contains an assembler and a vm-translator

## How to use

The entrypoint is defined in [Main.kt](src/main/kotlin/Main.kt)  
The main class can be run with the command `./gradlew -PmainClass=assembler.MainKt run --args='[...]'`    
Example of arguments are given below

### Parse a .asm file in a .hack file
`--args='--assembler --file example.asm'`

### Parse a .vm file in a .asm file
`--args='--vm-translator --file example.vm'`

### Print help
`--args='--help'`