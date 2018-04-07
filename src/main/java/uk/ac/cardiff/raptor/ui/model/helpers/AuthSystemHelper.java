package uk.ac.cardiff.raptor.ui.model.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import uk.ac.cardiff.raptor.ui.model.AuthSystem;

/**
 * Helper class when dealing with AuthSystem enums.
 * 
 * @author philsmart
 *
 */
@Service
public class AuthSystemHelper {

	private static final Logger log = LoggerFactory.getLogger(AuthSystemHelper.class);

	public AuthSystem[] getAuthSystems() {

		return AuthSystem.values();
	}

}
