import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

abstract class Command {
    abstract void execute(HashMap<String, Integer> registers, FileWriter outputFile) throws IOException;
}

class MovCommand extends Command {
    private String dest;
    private String src;

    public MovCommand(String dest, String src) {
        this.dest = dest;
        this.src = src;
    }

    void execute(HashMap<String, Integer> registers, FileWriter outputFile) throws IOException {
        if (registers.containsKey(src)) {
            int value = registers.get(src);
            registers.put(dest, value);
        } else {
            int value = Integer.parseInt(src);
            registers.put(dest, value);
        }
        outputFile.write(dest + " = " + registers.get(dest) + "\n");
    }
}

class AddCommand extends Command {
    private String dest;
    private String src;

    public AddCommand(String dest, String src) {
        this.dest = dest;
        this.src = src;
    }

    void execute(HashMap<String, Integer> registers, FileWriter outputFile) throws IOException {
        int value1 = registers.get(dest);
        int value2;
        if (registers.containsKey(src)) {
            value2 = registers.get(src);
        } else {
            value2 = Integer.parseInt(src);
        }
        int result = value1 + value2;
        registers.put(dest, result);
        outputFile.write(dest + " = " + result + "\n");
    }
}

class SubCommand extends Command {
    private String dest;
    private String src;

    public SubCommand(String dest, String src) {
        this.dest = dest;
        this.src = src;
    }

    void execute(HashMap<String, Integer> registers, FileWriter outputFile) throws IOException {
        int value1 = registers.get(dest);
        int value2;
        if (registers.containsKey(src)) {
            value2 = registers.get(src);
        } else {
            value2 = Integer.parseInt(src);
        }
        int result = value1 - value2;
        registers.put(dest, result);
        outputFile.write(dest + " = " + result + "\n");
    }
}

public class Assembly {
    public static void main(String[] args) {
        ArrayList<String> program = new ArrayList<>();
        program.add("MOV AX 10");
        program.add("MOV BX 5");
        program.add("ADD AX BX");
        program.add("SUB AX BX");
        program.add("MOV AX 20");
        program.add("MOV BX 15");
        program.add("SUB AX BX");
        program.add("ADD AX BX");
        program.add("ADD AX 35");
        program.add("SUB BX 5");
        program.add("ADD AX BX");

        HashMap<String, Integer> registers = new HashMap<>();
        registers.put("AX", 0);
        registers.put("BX", 0);

        try (FileWriter outputFile = new FileWriter("resultado.txt")) {
            for (String line : program) {
                String[] parts = line.split(" ");
                String command = parts[0];
                String dest = parts[1];
                String src = parts[2];

                Command cmd = null;
                if (command.equals("MOV")) {
                    cmd = new MovCommand(dest, src);
                } else if (command.equals("ADD")) {
                    cmd = new AddCommand(dest, src);
                } else if (command.equals("SUB")) {
                    cmd = new SubCommand(dest, src);
                }

                if (cmd != null) {
                    cmd.execute(registers, outputFile);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
