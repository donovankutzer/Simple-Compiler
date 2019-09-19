package v1;

public class Variable {
	private int value;
	private boolean initialized;

	public Variable() {
		initialized = false;
	}

	public void setValue(int v) {
		this.value = v;
		initialized = true;
	}

	public int getValue() throws UndefinedVariableException {
		if (!initialized)
			throw new UndefinedVariableException("Undefined Variable: " + value);
		else
			return value;
	}

	public boolean isInitialized() {
		return initialized;
	}
}
