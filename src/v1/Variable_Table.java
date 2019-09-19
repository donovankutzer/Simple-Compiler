package v1;

public class Variable_Table {
	Variable[] variable_table;

	// Array size 'z' which is 122. All other locations besides letter values will
	// be null.
	// Assign all letter array slots to new Variable construct

	public Variable_Table() {
		variable_table = new Variable[123];
		for (int i = 'a'; i <= 'z'; i++)
			variable_table[i] = new Variable();
		for (int i = 'A'; i <= 'Z'; i++)
			variable_table[i] = new Variable();
	}

	public void setVar(char var, int num) {
		variable_table[var].setValue(num);
	}

	public int getValue(char var) throws UndefinedVariableException {
		if (exists(var))
			return variable_table[var].getValue();
		throw new UndefinedVariableException();
	}

	public boolean exists(char var) {
		return variable_table[var].isInitialized();
	}
}
