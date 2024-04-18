import java.lang.StringBuilder;
import java.util.ArrayList;

public class Parser {
    private String[] src;
    private static String[] ex_words = {"from", "into", "refrigerator", "mixing", "bowl", "contents", "of", "the", "dry", "ingredients", "well", "for", "minutes", "baking", "dish", "aside", "until"};
    private static ArrayList<Object> values = new ArrayList<>();

    public Parser(String[] src) {
        this.src = src;
    }

    public String get_ingredient_name(String[] words){
        String ingredient_name = "";
        for (int j = 1; j < words.length; j++) {
            for(int i = 0; i < ex_words.length; i++){
                if (!words[j].equals(ex_words[i])){
                    ingredient_name += words[j];
                }
            }
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
    public Recipe parse() {
        ArrayList<Instruction> instructions = new ArrayList<>();
        StringBuilder comments = new StringBuilder();
        String cooking_time = "unknown";
        int temperature = 0;

        boolean parsing_comments = true;
        for (int i = 0; i < this.src.length; i++) {
            if (i > 0 && parsing_comments) {
                comments.append(this.src[i] + '\n');
                continue;
            }
            else if (this.src[i].contains("Ingredients.")) {
                parsing_comments = false;
                continue;
            }

            Instruction instruction = new Instruction();
            instruction.lexeme = this.src[i];
            instruction.line = i + 1;
            // Split string into words
            String[] words = this.src[i].split(" ");
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
                        
                    }
                    else{

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
                case "Refrigerate": { 
                    instruction.type = InstructionType.REFRIGERATE;
                    break;
                }
                //Any verb I guess, idk 
                // TODO: the rest!
            }
            instructions.add(instruction);
        }

        return new Recipe(instructions, this.src[0], comments.toString(), cooking_time, temperature);
    }
}