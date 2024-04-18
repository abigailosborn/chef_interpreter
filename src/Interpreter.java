import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Interpreter {
    Recipe recipe;
    HashMap<String, Object> variables;
    ArrayList<Stack<Object>> bowl;

    public Interpreter(Recipe recipe) {
        this.recipe = recipe;
        this.variables = new HashMap<>();
        this.bowl = new ArrayList<>();
    }

    public void run() throws ChefException {
        for (Instruction i : this.recipe.instructions) {
            System.out.println(i);
        }
    }
}