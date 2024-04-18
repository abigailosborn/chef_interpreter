public class Instruction {
    InstructionType type;
    Object[] values;
    String lexeme;
    int line;

    public String toString() {
        return "Instruction of type " + type.name() +
               " on line " + this.line + "\n:\n" +
               "\"" + this.lexeme + "\"";
    }
}