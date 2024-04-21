import java.util.ArrayList;

public class Recipe {
    ArrayList<Instruction> instructions;
    ArrayList<Ingredient> ingredients;
    String title;
    String comments;
    String cooking_time;
    int temperature;

    public Recipe(String title, ArrayList<Ingredient> ingredients,
                  ArrayList<Instruction> instructions, String comments,
                  String cooking_time, int temperature) {
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.title = title;
        this.comments = comments;
        this.cooking_time = cooking_time;
        this.temperature = temperature;
    }

    public String toString() {
        return this.title + '\n' + this.comments + '\n'
               + "Cook for " + this.cooking_time + " at "
               + this.temperature + " degrees";
    }
}