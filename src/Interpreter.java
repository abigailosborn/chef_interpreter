import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Interpreter {
    Recipe recipe;
    HashMap<String, Integer> ingredients;
    ArrayList<Stack<Integer>> bowl;

    public Interpreter(Recipe recipe) {
        this.recipe = recipe;

        this.ingredients = new HashMap<>();
        for (Ingredient i : recipe.ingredients)
            this.ingredients.put(i.name, i.inital_value);

        this.bowl = new ArrayList<>();
    }

    public void run() throws ChefException {
        for (Instruction i : this.recipe.instructions) {
            System.out.println(i);
        }
    }
}