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
	            continue; // handleWhileLoop adjusts the index
	        }
	        else if (line.startsWith("if")) {
	            index = handleIfStatement(lines, index);
	            continue;
	        }
	        else if (line.startsWith("for")) {
	            index = handleForLoop(lines, index);
	            continue; 
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
	private int handleForLoop(String[] lines, int index) {
	    String line = lines[index].trim();
	    if (!line.startsWith("for") || !line.contains("in") || !line.endsWith("{")) {
	        throw new IllegalArgumentException("Invalid for loop syntax: " + line);
	    }

	    // Parse the loop declaration
	    String[] parts = line.split("in");
	    String loopVariable = parts[0].trim().substring(4).trim(); // Extract the variable (e.g., "i")
	    String range = parts[1].trim().replace("{", ""); // Extract the range (e.g., "1...N" or "1..<N")

	    // Determine the start and end of the range
	    boolean isInclusive = range.contains("...");
	    String[] rangeParts = range.split("\\.\\.\\.");
	    int start = evaluateExpression(rangeParts[0].trim());
	    int end = evaluateExpression(rangeParts[1].trim());
	    if (!isInclusive) {
	        end--; // Adjust for exclusive range ("..<")
	    }

	    // Collect the loop body
	    StringBuilder loopBody = new StringBuilder();
	    index++; // Move to the next line after "for i in range {"
	    while (index < lines.length && !lines[index].trim().equals("}")) {
	        loopBody.append(lines[index].trim()).append("\n");
	        index++;
	    }

	    // Execute the loop
	    for (int i = start; i <= end; i++) {
	        variables.put(loopVariable, i); // Update the loop variable
	        eval(loopBody.toString()); // Execute the loop body
	    }

	    return index; // Return the index after the loop body
	}

	private int handleIfStatement(String[] lines, int index) {
	    String line = lines[index].trim();
	    if (!line.startsWith("if") || !line.endsWith("{")) {
	        throw new IllegalArgumentException("Invalid if statement syntax: " + line);
	    }

	    // Extract the condition inside the parentheses, remove "if " and "{"
	    String condition = line.substring(2, line.length() - 1).trim(); // Remove "if" and "{"
	// Collect all lines in the if block until we encounter "}"
	StringBuilder ifBlock = new StringBuilder();
	index++; // Move to the next line after "if condition {"

	while (index < lines.length && !lines[index].trim().equals("}")) {
	    ifBlock.append(lines[index].trim()).append("\n");
	    index++;
	}

	// Evaluate the condition
	if (evaluateCondition(condition)) {
	    eval(ifBlock.toString()); // Execute the if block
	}

	return index; // Return the index after the if block
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
		// Check for boolean literals
	    if (condition.equals("true")) return true;
	    if (condition.equals("false")) return false;

	    String[] parts = condition.split("<=|>=|<|>|==|!=");
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
	    if (condition.contains("!=")) return leftValue != rightValue;
	    throw new IllegalArgumentException("Invalid condition: " + condition);
	}

	private int evaluateExpression(String expression) {
	    // Remove any extra whitespace
	    expression = expression.trim();

	    // Check for multiplication, division, addition, or subtraction
	    if (expression.contains("*")) {
	        String[] tokens = expression.split("\\*");
	        int result = 1;
	        for (String token : tokens) {
	            token = token.trim();
	            result *= variables.containsKey(token) ? variables.get(token) : Integer.parseInt(token);
	        }
	        return result;
	    } else if (expression.contains("/")) {
	        String[] tokens = expression.split("/");
	        int result = variables.containsKey(tokens[0].trim()) ? variables.get(tokens[0].trim()) : Integer.parseInt(tokens[0].trim());
	        for (int i = 1; i < tokens.length; i++) {
	            String token = tokens[i].trim();
	            int value = variables.containsKey(token) ? variables.get(token) : Integer.parseInt(token);
	            result /= value;
	        }
	        return result;
	    } else if (expression.contains("-")) {
	        String[] tokens = expression.split("-");
	        int result = variables.containsKey(tokens[0].trim()) ? variables.get(tokens[0].trim()) : Integer.parseInt(tokens[0].trim());
	        for (int i = 1; i < tokens.length; i++) {
	            String token = tokens[i].trim();
	            int value = variables.containsKey(token) ? variables.get(token) : Integer.parseInt(token);
	            result -= value;
	        }
	        return result;
	    } else if (expression.contains("+")) {
	        String[] tokens = expression.split("\\+");
	        int sum = 0;
	        for (String token : tokens) {
	            token = token.trim();
	            sum += variables.containsKey(token) ? variables.get(token) : Integer.parseInt(token);
	        }
	        return sum;
	    }
	    else if (expression.contains("%")) {
	        String[] tokens = expression.split("\\%");
	        int result = variables.containsKey(tokens[0].trim()) ? variables.get(tokens[0].trim()) : Integer.parseInt(tokens[0].trim());
	        for (String token : tokens) {
	            token = token.trim();
	            result %= variables.containsKey(token) ? variables.get(token) : Integer.parseInt(token);
	        }
	        return result;
	    }

	    // If no operator, it must be a single number or variable
	    return variables.containsKey(expression) ? variables.get(expression) : Integer.parseInt(expression);

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
	}     
