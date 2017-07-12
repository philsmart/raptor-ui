package uk.ac.cardiff.raptor.ui.service;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import uk.ac.cardiff.raptor.ui.model.SystemSelection;

@Service
final public class SearchToSqlMapper {

	@Value("${raptorui.sql.mapper.tablename.shibboleth:SHIB_IDP_AUTH_EVENT}")
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
	 * possibilites.
	 * 
	 * @param system
	 * @return
	 */
	public String mapToTableName(final SystemSelection.SYSTEM system) {
		if (system == SystemSelection.SYSTEM.SHIBBOLETH) {
			return shibbolethTableName;
		} else if (system == SystemSelection.SYSTEM.EZPROXY) {
			return ezproxyTableName;
		}
		// can never happen,
		return shibbolethTableName;

	}

	public Optional<String> mapToTableName(final SystemSelection system) {
		if (system.isShibboleth()) {
			return Optional.of(shibbolethTableName);
		} else if (system.isEzproxy()) {
			return Optional.of(ezproxyTableName);
		}
		return Optional.empty();

	}

}
