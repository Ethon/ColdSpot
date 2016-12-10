package cc.ethon.coldspot.frontend.errors;

import cc.ethon.coldspot.common.InvalidClassFileException;

public class InvalidLocalVariableException extends InvalidClassFileException {

	private static final long serialVersionUID = -3790908564780362085L;

	public InvalidLocalVariableException() {
		super();
	}

	public InvalidLocalVariableException(String message) {
		super(message);
	}

	public InvalidLocalVariableException(Throwable cause) {
		super(cause);
	}

	public InvalidLocalVariableException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidLocalVariableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
