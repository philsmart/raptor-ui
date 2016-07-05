package uk.ac.cardiff.raptor.ui.service;

public class DashboardException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1847316553078929877L;

	/**
	 * 
	 */
	public DashboardException() {
		super();

	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public DashboardException(final String message, final Throwable cause, final boolean enableSuppression,
			final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);

	}

	/**
	 * @param message
	 * @param cause
	 */
	public DashboardException(final String message, final Throwable cause) {
		super(message, cause);

	}

	/**
	 * @param message
	 */
	public DashboardException(final String message) {
		super(message);

	}

	/**
	 * @param cause
	 */
	public DashboardException(final Throwable cause) {
		super(cause);

	}

}
