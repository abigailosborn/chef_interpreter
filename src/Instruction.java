public class Instruction {
    InstructionType type;
    Object[] values;
    String lexeme;
    int line;

    public String toString() {
        String string = "Instruction of type " + type.name() +
                        " on line " + this.line + " with values [";
        if (this.values != null) {
            for (int i = 0; i < this.values.length; i++) {
                string += this.values[i];
                if (i < this.values.length - 1)
                    string += ", ";
            }
        }
        string += "]:\n" + this.lexeme;

        return string;
    }
}