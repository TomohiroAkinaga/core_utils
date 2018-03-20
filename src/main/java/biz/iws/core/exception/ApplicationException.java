package biz.iws.core.exception;

/**
 * @author Tomohiro Akinaga
 */
public class ApplicationException extends RuntimeException {

	private static final long serialVersionUID = 3089044590454413333L;

	/**
	 * Default constructor.
	 */
	public ApplicationException() {}

	/**
	 * Constructor with cause throwable.
	 * @param t root cause
	 */
	public ApplicationException(Throwable t) {
		this(t, t.getMessage());
	}

	/**
	 * Constructor with log message.
	 * @param logMessage
	 */
	public ApplicationException(String logMessage) {
		super(logMessage);
	}

	/**
	 * Full constructor.
	 * @param t root cause
	 * @param logMessage ログ出力メッセージ
	 */
	public ApplicationException(Throwable t, String logMessage) {
		super(logMessage, t);
	}

	/**
	 * ログ出力用メッセージを取得します。
	 * @return ログ出力用メッセージ
	 */
	public String getLogMessage() {
		return super.getMessage();
	}

	/**
	 * root causeを取得します。
	 * @return root cause
	 */
	public Throwable getCause() {
		return super.getCause();
	}
}
