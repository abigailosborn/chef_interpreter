import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.Arrays;

public class Parser {
    private String[] src;
    private int current_line;
    private static ArrayList<String> ex_words = new ArrayList<>(Arrays.asList("from", "into", "refrigerator", "mixing", "bowl", "contents", "of", "the", "dry", "ingredients", "well", "for", "minutes", "baking", "dish", "aside", "until"));
    private static ArrayList<Object> values = new ArrayList<>();

    public Parser(String[] src) {
        this.src = src;
        this.current_line = 0;
    }

    public String get_ingredient_name(String[] words){
        String ingredient_name = "";
        for (int j = 1; j < words.length && !ex_words.contains(words[j]); j++) {
            ingredient_name += words[j];
            // Only add a space between words when appropriate
            if (j < words.length - 2 && !ex_words.contains(words[j+1]))
                ingredient_name += " ";
        }
        return ingredient_name;
    }
    private boolean is_number(String word){
        try{
            Integer.parseInt(word);
            return true;
        }
        catch(Exception E){
            return false;
        }
    }

    private ArrayList<Instruction> parse_instructions() {
        ArrayList<Instruction> instructions = new ArrayList<>();

        /* The "Method." label is the current line when
        we enter this function, so increment it */
        this.current_line++;
        for (; this.current_line < this.src.length; this.current_line++) {
            // TODO: break on auxilary recipes

            if (this.src[this.current_line].length() < 1)
                continue;

            Instruction instruction = new Instruction();
            instruction.lexeme = this.src[this.current_line];
            instruction.line = this.current_line + 1;
            // Split string into words
            String[] words = this.src[this.current_line].split(" ");
            String first_word = words[0];
            switch (first_word) {
                case "Take": {
                    ArrayList<String> values = new ArrayList<>();
                    values.add(get_ingredient_name(words));
                    instruction.values = values.toArray(new String[0]);
                    instruction.type = InstructionType.TAKE_FROM_FRIDGE;
                    break;
                }
                case "Put": { 
                    Integer mixing_bowl_num = 0;
                    String ingredient = get_ingredient_name(words);
                    int spaces = ingredient.split(" ").length;
                    if(is_number(words[2 + spaces])){
                        mixing_bowl_num = Integer.parseInt(words[2 + spaces]);
                    }
                    else{
                        mixing_bowl_num = 0;
                    }
                    values.add(ingredient);
                    values.add(mixing_bowl_num);
                    instruction.values = values.toArray(new Object[0]);
                    instruction.type = InstructionType.PUT_INTO_BOWL;
                    break;
                }
                case "Fold": { 
                    instruction.type = InstructionType.FOLD_INTO_BOWL;
                    break;
                }
                //there's two add functions 
                case "Add": { 
                    if(words[1].equals("dry")){
                        instruction.type = InstructionType.ADD_INTO_BOWL;
                    }
                    else{
                        instruction.type = InstructionType.ADD_DRY_INGREDIENTS;
                    }
                    break;
                }
                case "Remove": { 
                    instruction.type = InstructionType.REMOVE_FROM_BOWL;
                    break;
                }
                case "Combine": { 
                    instruction.type = InstructionType.COMBINE;
                    break;
                }
                case "Divide": { 
                    instruction.type = InstructionType.DIVIDE;
                    break;
                }
                //Two of these too
                case "Liquefy": { 
                    if(words[1].equals("contents")){
                        instruction.type = InstructionType.LIQUEFY_CONTENTS_OF_BOWL;
                    }
                    else{
                        instruction.type = InstructionType.LIQUEFY;
                    }
                    break;
                }
                //Once again two 
                case "Stir": { 
                    if(is_number(words[1])){
                        instruction.type = InstructionType.STIR_FOR_N_MINS;
                    }
                    else{
                        instruction.type = InstructionType.STIR_INTO_BOWL;
                    }
                    break;
                }
                case "Mix": { 
                    instruction.type = InstructionType.MIX_WELL;
                    break;
                }
                case "Clean": { 
                    instruction.type = InstructionType.CLEAN_BOWL;
                    break;
                }
                case "Pour": { 
                    instruction.type = InstructionType.POUR_BOWL_INTO_DISH;
                    break;
                }
                case "Set": { 
                    instruction.type = InstructionType.SET_ASIDE; 
                    break;
                }
                case "Serve": { 
                    instruction.type = InstructionType.SERVE_WITH;
                    break;
                }
                case "Serves": { 
                    instruction.type = InstructionType.SERVES;
                    break;
                }
                case "Refrigerate": { 
                    instruction.type = InstructionType.REFRIGERATE;
                    break;
                }
                //Any verb I guess, idk 
                // TODO: the rest!
            }
            instructions.add(instruction);
        }
        return instructions;
    }

    private ArrayList<Ingredient> parse_ingredients() {
        ArrayList<Ingredient> ingredients = new ArrayList<>();

        /* The "Ingredients." label is the current line when
        we enter this function, so increment it */
        this.current_line++;
        for (; this.current_line < this.src.length; this.current_line++) {
            /* TODO: tell the user that ingredient
            declarations shouldn't end with periods */
            if (this.src[this.current_line].contains("."))
                break;
            else if (this.src[this.current_line].length() < 1)
                continue;

            Ingredient ingredient = new Ingredient();
            String[] words = this.src[this.current_line].split(" ");
            int words_before_name = 0;

            if (is_number(words[0])) {
                ingredient.inital_value = Integer.parseInt(words[0]);
                words_before_name++;
            }
            
            if (words[words_before_name].equals("heaped") || words[words_before_name].equals("level")) {
                ingredient.is_heaped_or_level = true;
                words_before_name++;
            }

            ingredient.measure = switch (words[words_before_name].toLowerCase()) {
                case "g"                         -> Measure.G;
                case "kg"                        -> Measure.KG;
                case "pinch", "pinches"          -> Measure.PINCH;
                case "ml"                        -> Measure.ML;
                case "l"                         -> Measure.L;
                case "dash", "dashes"            -> Measure.DASH;
                case "cup", "cups"               -> Measure.CUP;
                case "teaspoon", "teaspoons"     -> Measure.TEASPOON;
                case "tablespoon", "tablespoons" -> Measure.TABLESPOON;
                default                          -> Measure.UNDEFINED;
            };
            if (ingredient.measure != Measure.UNDEFINED)
                words_before_name++;

            // Parse the ingredient name
            StringBuilder sb = new StringBuilder();
            for (int i = words_before_name; i < words.length; i++)
                sb.append(words[i] + (i < words.length - 1 ? " " : ""));
            ingredient.name = sb.toString();

            ingredients.add(ingredient);
        }
    
        return ingredients;
    }

    public Recipe parse() {
        ArrayList<Instruction> instructions = new ArrayList<>();
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        StringBuilder comments = new StringBuilder();
        String cooking_time = "unknown";
        int temperature = 0;

        for (; this.current_line < this.src.length && this.current_line < this.src.length; this.current_line++) {
            if (this.src[this.current_line].startsWith("Ingredients."))
                ingredients.addAll(parse_ingredients());
            if (this.src[this.current_line].startsWith("Method."))
                instructions.addAll(parse_instructions());

            if (this.current_line < this.src.length) {
                if (this.src[this.current_line].startsWith("Cooking time: ")) {
                    String[] words = this.src[this.current_line].split(" ");
                    cooking_time = words[2] + words[3];
                }
                else if (this.src[this.current_line].startsWith("Pre-heat")) {
                    String[] words = this.src[this.current_line].split(" ");
                    temperature = Integer.parseInt(words[3]);
                }
                else if (this.current_line > 0)
                    comments.append(this.src[this.current_line] + '\n');
            }
        }

        return new Recipe(this.src[0], ingredients, instructions, comments.toString(), cooking_time, temperature);
    }
}