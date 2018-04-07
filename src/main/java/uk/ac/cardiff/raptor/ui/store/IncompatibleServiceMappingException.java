/**
 * 
 */
package uk.ac.cardiff.raptor.ui.store;

/**
 * Thrown to indicate a fundamental failure of a {@link ServiceMappingsStorage}
 * method where the supplied type or subtype was not compatible with the
 * implementing class.
 * 
 * @author philsmart
 *
 */
public class IncompatibleServiceMappingException extends RuntimeException {

	/**
	 * default serial uid.
	 */
	private static final long serialVersionUID = 5167063219031090147L;

	public IncompatibleServiceMappingException() {

	}

	public IncompatibleServiceMappingException(final String message) {
		super(message);

	}

	public IncompatibleServiceMappingException(final Throwable cause) {
		super(cause);

	}

	public IncompatibleServiceMappingException(final String message, final Throwable cause) {
		super(message, cause);

	}

	public IncompatibleServiceMappingException(final String message, final Throwable cause,
			final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);

	}

}
