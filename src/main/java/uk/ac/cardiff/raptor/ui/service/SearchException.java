package uk.ac.cardiff.raptor.ui.service;

public class SearchException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1847316553078929877L;

	/**
	 * 
	 */
	public SearchException() {
		super();

	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public SearchException(final String message, final Throwable cause, final boolean enableSuppression,
			final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);

	}

	/**
	 * @param message
	 * @param cause
	 */
	public SearchException(final String message, final Throwable cause) {
		super(message, cause);

	}

	/**
	 * @param message
	 */
	public SearchException(final String message) {
		super(message);

	}

	/**
	 * @param cause
	 */
	public SearchException(final Throwable cause) {
		super(cause);

	}

}
