package uk.ac.cardiff.raptor.ui.service;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import uk.ac.cardiff.raptor.ui.model.AuthSystem;
import uk.ac.cardiff.raptor.ui.model.SystemSelection;

@Service
final public class SearchToSqlMapper {

	@Value("${raptorui.sql.mapper.tablename.shibboleth:shib_idp_auth_event}")
	private String shibbolethTableName;

	@Value("${raptorui.sql.mapper.tablename.ezproxy:ezproxy_auth_event}")
	private String ezproxyTableName;

	@PostConstruct()
	public void init() {
		Objects.requireNonNull(shibbolethTableName);
		Objects.requireNonNull(ezproxyTableName);
	}

	/**
	 * Returns String as opposed to optional, as there are a known fixed set of
	 * Possibilities.
	 * 
	 * @param system
	 *            the {@link SYSTEM} to select a table name for.
	 * @return a string that represents the database tablename that the input
	 *         {@link SYSTEM} represents.
	 */
	public String mapToTableName(final AuthSystem system) {
		if (system == AuthSystem.SHIBBOLETH_IDP) {
			return shibbolethTableName;
		} else if (system == AuthSystem.EZPROXY) {
			return ezproxyTableName;
		}
		// can never happen,
		return shibbolethTableName;

	}

	public Optional<String> mapToTableName(final SystemSelection system) {
		return Optional.of(mapToTableName(system.getSelected()));

	}

}
