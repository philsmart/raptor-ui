/**
 * 
 */
package uk.ac.cardiff.raptor.ui.service;

/**
 * @author philsmart
 *
 */
public class UnauthorisedSearchException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3719071896062725984L;

	/**
	 * 
	 */
	public UnauthorisedSearchException() {

	}

	/**
	 * @param message
	 */
	public UnauthorisedSearchException(final String message) {
		super(message);

	}

	/**
	 * @param cause
	 */
	public UnauthorisedSearchException(final Throwable cause) {
		super(cause);

	}

	/**
	 * @param message
	 * @param cause
	 */
	public UnauthorisedSearchException(final String message, final Throwable cause) {
		super(message, cause);

	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public UnauthorisedSearchException(final String message, final Throwable cause, final boolean enableSuppression,
			final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);

	}

}
