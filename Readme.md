This is an assembler for the project 6 of https://www.nand2tetris.org/course

## How to use

The entrypoint is defined in [Main.kt](src/main/kotlin/Main.kt)  
The main class can be run with the command `./gradlew -PmainClass=assembler.MainKt run --args='[...]'`    
Example of arguments are given below

#### Parse a .asm file in a .hack file
`--args='--assembler --file example.asm'`

### Print help
`--args='--help'`