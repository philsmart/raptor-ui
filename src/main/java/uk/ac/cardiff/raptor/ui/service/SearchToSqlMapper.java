package uk.ac.cardiff.raptor.ui.service;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import uk.ac.cardiff.raptor.ui.model.SystemSelection;

@Service
final public class SearchToSqlMapper {

	@Value("${raptorui.sql.mapper.tablename.shibboleth:shibidpauthe}")
	private String shibbolethTableName;

	@Value("${raptorui.sql.mapper.tablename.ezproxy:ezproxyauthe}")
	private String ezproxyTableName;

	@PostConstruct()
	public void init() {
		Objects.requireNonNull(shibbolethTableName);
		Objects.requireNonNull(ezproxyTableName);
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
