package v1;

public class UndefinedVariableException extends Exception {
	public UndefinedVariableException() {

	}

	public UndefinedVariableException(String message) {
		super(message);
	}

	public UndefinedVariableException(Throwable cause) {
		super(cause);
	}

	public UndefinedVariableException(String message, Throwable cause) {
		super(message, cause);
	}
}
