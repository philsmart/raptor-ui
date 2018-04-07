package uk.ac.cardiff.raptor.ui.store;

/**
 * Failure in
 * 
 * @author philsmart
 *
 */
public class ServiceMappingsNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2800699204225630781L;

	/**
	 * 
	 */
	public ServiceMappingsNotFoundException() {
		super();

	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public ServiceMappingsNotFoundException(final String message, final Throwable cause,
			final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);

	}

	/**
	 * @param message
	 * @param cause
	 */
	public ServiceMappingsNotFoundException(final String message, final Throwable cause) {
		super(message, cause);

	}

	/**
	 * @param message
	 */
	public ServiceMappingsNotFoundException(final String message) {
		super(message);

	}

	/**
	 * @param cause
	 */
	public ServiceMappingsNotFoundException(final Throwable cause) {
		super(cause);

	}

}
