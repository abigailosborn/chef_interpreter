import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

//Anyone can Cook :)
public class Gusteau {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("No source file provided!");
            return;
        }
        String filename = args[0];
        File input_file = new File(filename);

        Scanner file_reader;
        try {
            file_reader = new Scanner(input_file);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            // TODO: resource leak...
            return;
        }

        ArrayList<String> src_lines = new ArrayList<>();
        while(file_reader.hasNextLine())
        {
            src_lines.add(file_reader.nextLine());
        }
        file_reader.close();

        Parser parser = new Parser(src_lines.toArray(new String[0]));
        Recipe recipe = parser.parse();

        Interpreter interpreter = new Interpreter(recipe);
        try {
            interpreter.run();
        }
        catch (ChefException e) {
            System.err.println(e.getMessage());
        }
    }
}