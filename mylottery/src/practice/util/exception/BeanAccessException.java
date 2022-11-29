package practice.util.exception;

public class BeanAccessException extends RootException{

	/**
	 * <p>
	 * Description:获取相应异常的键值
	 * </p>
	 * 
	 * @return
	 */
	@Override
	public String getKey() {
		return GlobalConstants.EXCEPTION_BEAN_ACCESS;
	}

	/**
	 * @param message
	 */
	public BeanAccessException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param args
	 */
	public BeanAccessException(String message, String[] args) {
		super(message, args);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public BeanAccessException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param args
	 * @param cause
	 */
	public BeanAccessException(String message, String[] args, Throwable cause) {
		super(message, args, cause);
	}
}
