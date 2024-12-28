package swiftInt;
import java.util.HashMap;
import java.util.Map;
public class SwiftInterpreter {
    private final Map<String, Integer> variables = new HashMap<>(); // Variable storage
    public void eval(String input) {
        String[] lines = input.split("\n"); // Split by newlines
        int index = 0;
        while (index < lines.length) {
            String line = lines[index].trim(); // Trim whitespace
            if (line.isEmpty()) {
                index++;
                continue; // Skip empty lines
            }
            // Handle variable declarations (let or var)
            if (line.startsWith("let ") || line.startsWith("var ")) {
                handleVariableDeclaration(line);
            }
            // Handle while loops
            else if (line.startsWith("while")) {
                index = handleWhileLoop(lines, index);
                continue; // `handleWhileLoop` adjusts the index
            }
            if (line.endsWith("}")) {
            	index++;
            	continue;
            }
            // Handle expressions and assignments (e.g., sum = sum + i)
            else if (line.contains("=")) {
                handleAssignment(line);
            }
            // Handle print statements
            else if (line.startsWith("print")) {
                handlePrint(line);
            } else {
                throw new IllegalArgumentException("Unknown statement: " + line);
            }
            index++;
        }
    }
    private void handleVariableDeclaration(String line) {
        if (!line.contains("=")) {
            throw new IllegalArgumentException("Invalid variable declaration: " + line);
        }

        String[] parts = line.split("=");
        String declaration = parts[0].trim();
        String value = parts.length > 1 ? parts[1].trim() : "0"; // Default value is 0

        String varName = declaration.split(" ")[1]; // Extract variable name
        
        variables.put(varName, evaluateExpression(value));
    }

    private int handleWhileLoop(String[] lines, int index) {
        // Handle the while condition, after "while"
        String line = lines[index].trim();
        if (!line.startsWith("while") || !line.endsWith("{")) {
            throw new IllegalArgumentException("Invalid while loop syntax: " + line);
        }
        // Extract the condition inside the parentheses, remove "while " and "{"
        String condition = line.substring(5, line.length() - 1).trim(); // Remove "while" and "{"
        // Collect all lines in the loop body until we encounter "}"
        StringBuilder loopBody = new StringBuilder();
        index++; // Move to the next line after "while condition {"
        while (index < lines.length && !lines[index].trim().equals("}")) {
            loopBody.append(lines[index].trim()).append("\n");
            index++;
        }
        // Evaluate the loop
        while (evaluateCondition(condition)) {
            eval(loopBody.toString()); // Execute the loop body
        }
        return index; // Return the index after the loop, which should be at the line containing "}"
    }
    private boolean evaluateCondition(String condition) {
        // Split condition (e.g., "i<=n") into parts
        String[] parts = condition.split("<=|>=|<|>|==");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid condition: " + condition);
        }
        String left = parts[0].trim();
        String right = parts[1].trim();
        int leftValue = variables.containsKey(left) ? variables.get(left) : Integer.parseInt(left);
        int rightValue = variables.containsKey(right) ? variables.get(right) : Integer.parseInt(right);

        if (condition.contains("<=")) return leftValue <= rightValue;
        if (condition.contains(">=")) return leftValue >= rightValue;
        if (condition.contains("<")) return leftValue < rightValue;
        if (condition.contains(">")) return leftValue > rightValue;
        if (condition.contains("==")) return leftValue == rightValue;

        throw new IllegalArgumentException("Invalid condition: " + condition);
    }

    private int evaluateExpression(String expression) {
        // Split the expression into terms (e.g., sum + i)
        String[] tokens = expression.split("\\+");
        int sum = 0;
        for (String token : tokens) {
            token = token.trim();
            sum += variables.containsKey(token) ? variables.get(token) : Integer.parseInt(token);
        }
        return sum;
    }

    private void handleAssignment(String line) {
        String[] parts = line.split("=");
        String varName = parts[0].trim();
        String expression = parts[1].trim();
        
        // Evaluate the right-hand side expression
        int value = evaluateExpression(expression);
        variables.put(varName, value);
    }

    private void handlePrint(String line) {
        String varName = line.substring(line.indexOf('(') + 1, line.indexOf(')')).trim();
       
        System.out.println(variables.getOrDefault(varName, 0)); // Print variable value or 0 if undefined
    }

    public static void main(String[] args) {
        SwiftInterpreter interpreter = new SwiftInterpreter();

        String program = """
            let n=10
            var sum=0
            var i=1
            while i<=n {
                sum=sum+i
                i=i+1 
                }
            print(sum)
        """;
 
        interpreter.eval(program); // This should execute the program and print 55
       
    }
}