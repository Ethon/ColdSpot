package cc.ethon.coldspot.common;

public class InvalidClassFileException extends Exception {

	private static final long serialVersionUID = 2276990318821040979L;

	public InvalidClassFileException() {
		super();
	}

	public InvalidClassFileException(String message) {
		super(message);
	}

	public InvalidClassFileException(Throwable cause) {
		super(cause);
	}

	public InvalidClassFileException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidClassFileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
