package v1;

import java.util.*;
import java.io.*;

public class Compiler {
	public static void main(String[] args) throws UndefinedVariableException, FileNotFoundException {
		// Initialize table of Variables
		Variable_Table table = new Variable_Table();

		// To access and manipulate file in args[0]
		File file = new File(args[0]);
		Scanner input = new Scanner(file);

		// Scanner for read. Must be out of read method in case multiple occurrences of
		// read. Once closing the scanner, will throw error if trying to make it again
		Scanner userIn = new Scanner(System.in);

		// Will prevent resource leaks
		try {
			// Re iterate until input.esp has no more tokens to throw
			while (input.hasNext()) {

				// Line will access the next available line in the file
				String line = input.nextLine();
				// Initial will take the first token in the line, I.E. the initial command
				String initial = line.substring(0, line.indexOf(" "));
				// Command will take everything after the first space, I.E. the command
				String command = line.substring(line.indexOf(" ") + 1, line.length());

				// Execute read method
				if (initial.equalsIgnoreCase("read")) {
					read(table, userIn, command);
				}

				// Execute print method
				else if (initial.equalsIgnoreCase("print")) {
					print(table, command);
				}

				// Will process the infix to postfix and return the resulting value into the
				// proper variable array spot
				else if (initial.length() == 1 && Character.isLetter(initial.charAt(0))) {
					varEdit(table, initial, command);
				}

				// If no proper commands, will throw Exception
				else
					throw new IllegalArgumentException();
			}
		} finally {
			// Close all Scanners before ending
			input.close();
			userIn.close();
			// Terminate program once done executing
			System.exit(0);
		}
	}

	public static void read(Variable_Table table, Scanner userIn, String var)
			throws IllegalArgumentException, UndefinedVariableException {

		// If variable after "read " is not 1 character, is illegal argument
		if (var.length() != 1)
			throw new IllegalArgumentException();
		// Converts string to char for sake of variable_table
		char cVar = var.charAt(0);
		// Take user input
		System.out.println("Enter the value you'd like to set to " + cVar + ": ");
		int number = userIn.nextInt();
		// Initialize and set value of variable in table
		table.setVar(cVar, number);

	}

	public static void varEdit(Variable_Table table, String var, String command)
			throws IllegalArgumentException, UndefinedVariableException {
		// Convert String to char
		char cVar = var.charAt(0);

		// Enforcing proper syntax. If there is no = after the initial variable, throw
		// Exception
		if (!command.substring(0, 1).equals("="))
			throw new IllegalArgumentException();

		// Remove = sign for proper use of inToPostFix method
		String commandAfterEquals = command.substring(2, command.length());

		// Execute both methods to get analyze equation and set variable to the integer
		// given
		table.setVar(cVar, evalPostFix(inToPostFix(table, commandAfterEquals), table));
	}

	public static String inToPostFix(Variable_Table table, String command)
			throws IllegalArgumentException, UndefinedVariableException {
		// Create string which will then be added to as while loop progresses
		String pfix = "";
		// isValid will check if any of if statements have been done. If not, throw
		// exception
		boolean isValid = true;

		// Creates stack for symbols
		Stack<Character> symStack = new Stack<Character>();

		// Converts command String to a Scanner, that way .next() and .hasNext() are
		// usable.
		Scanner input = new Scanner(command);
		try {
			// Will run until end of line
			while (input.hasNext()) {
				// Next token to evaluate
				String token = input.next();
				isValid = false;

				// if token is a Variable
				if (Character.isLetter(token.charAt(0))) {
					if (token.length() != 1)
						throw new IllegalArgumentException();
					char cVar = token.charAt(0);
					pfix += table.getValue(cVar);
					isValid = true;

				}

				// if token is a any length of number
				else if (Character.isDigit(token.charAt(0))) {
					// loop for if number contains any illegal characters after initial location.
					// Will advance to next non-digit value
					for (int i = 0; i < token.length(); i++) {
						if (!Character.isDigit(token.charAt(i)))
							throw new IllegalArgumentException();
					}
					pfix += token;
					isValid = true;
				}

				else if (token.equals("(")) {
					symStack.push(token.charAt(0));
					isValid = true;
				}

				else if (token.equals(")")) {
					while (symStack.peek() != '(') {
						pfix += symStack.pop();
					}
					// To pop the "(" symbol
					symStack.pop();
					isValid = true;
				}

				// If token is any symbol
				else
					switch (token) {
					case "+":
					case "-":
					case "*":
					case "/":
					case "%":
						if (!symStack.empty()) {
							while (!symStack.empty() && precedence(token.charAt(0)) < precedence(symStack.peek()))
								pfix += symStack.pop();
							symStack.push(token.charAt(0));
						} else if (symStack.empty()) {
							symStack.push(token.charAt(0));
						}
						isValid = true;
					}

				// If none of the above conditionals get used, isValid will be false which means
				// syntax error
				if (isValid == false)
					throw new IllegalArgumentException();

				// When pushing symbol to stack, there will be an additional space added. This
				// will avoid that
				if (pfix.length() > 0 && (pfix.charAt(pfix.length() - 1) != ' '))
					pfix += " ";
			}

			// Will pop any remaining symbols in stack to finish the postfix evaluation
			while (!symStack.empty()) {
				pfix += symStack.pop() + " ";
			}
		} finally {
			// To prevent resource leaks
			input.close();
		}
		return pfix;
	}

	public static int evalPostFix(String postfix, Variable_Table table) {
		// Creates a stack for the integers
		Stack<Integer> intStack = new Stack<Integer>();

		// Evaluates every character in the postfix String
		for (int i = 0; i < postfix.length(); i++) {
			// Ignores whitespace
			if (postfix.charAt(i) == ' ')
				continue;

			else if (Character.isDigit(postfix.charAt(i))) {
				int k = i;
				// Will evaluate how many characters are digits, and will add the total number
				// to the stack
				while (Character.isDigit(postfix.charAt(i))) {
					i++;
				}
				int num = Integer.valueOf(postfix.substring(k, i));
				intStack.push(num);
			} else {

				// Takes top 2 numbers in stack
				int num2 = intStack.pop();
				int num1 = intStack.pop();

				// Will then take next symbol, evaluate the 2 numbers with the symbol, and push
				// the number back to the stack
				switch (postfix.charAt(i)) {
				case '+':
					intStack.push(num1 + num2);
					break;
				case '-':
					intStack.push(num1 - num2);
					break;
				case '*':
					intStack.push(num1 * num2);
					break;
				case '/':
					intStack.push(num1 / num2);
					break;
				case '%':
					intStack.push(num1 % num2);
					break;
				}
			}
		}
		// Returns final number
		return intStack.pop();
	}

	// To determine what precedence each symbol has over the other
	public static int precedence(char symbol) {
		switch (symbol) {
		case '+':
		case '-':
			return 1;
		case '*':
		case '/':
		case '%':
			return 2;
		// To suppress this method requiring int as return. Will never return -1 because
		// all chars are evaluated beforehand.
		default:
			return -1;
		}
	}

	public static void print(Variable_Table table, String input) throws UndefinedVariableException {
		// Will evaluate whatever is given after print statement and print out
		System.out.println(evalPostFix(inToPostFix(table, input), table));
	}

}