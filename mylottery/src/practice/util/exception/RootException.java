package practice.util.exception;

public abstract class RootException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8250124272001825153L;

	/**
	 * <p>
	 * Description:获取相应异常的键值
	 * </p>
	 * 
	 * @return
	 */
	public abstract String getKey();

	/**
	 * @param message
	 */
	public RootException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param args
	 */
	public RootException(String message, String[] args) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public RootException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param args
	 * @param cause
	 */
	public RootException(String message, String[] args, Throwable cause) {
		super(message, cause);
	}
}
