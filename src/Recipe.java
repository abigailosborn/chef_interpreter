import java.util.ArrayList;

public class Recipe {
    ArrayList<Instruction> instructions;
    String title;
    String comments;
    String cooking_time;
    int temperature;

    public Recipe(ArrayList<Instruction> instructions, String title,
                  String comments, String cooking_time, int temperature) {
        this.instructions = instructions;
        this.title = title;
        this.comments = comments;
        this.temperature = temperature;
    }

    public String toString() {
        return this.title + '\n' + this.comments + '\n'
               + "Cook for " + this.cooking_time + "at"
               + this.temperature + "degrees";
    }
}