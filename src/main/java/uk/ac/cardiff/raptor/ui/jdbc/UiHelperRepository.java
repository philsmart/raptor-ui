package uk.ac.cardiff.raptor.ui.jdbc;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * A {@link Repository} for loading values from the underlaying database to
 * support the UI e.g. autocompletes etc.
 * 
 * @author philsmart
 *
 */
@Repository
public class UiHelperRepository {

	private static final Logger log = LoggerFactory.getLogger(UiHelperRepository.class);

	@Value("${raptorui.sql.ui.autocomplete.service-providers}")
	private String findAllServiceProviders;

	@Value("${raptorui.sql.ui.autocomplete.departments}")
	private String findAllDepartments;

	@Value("${raptorui.sql.ui.autocomplete.serviceIDs}")
	private String findAllServiceIDs;

	@Inject
	private JdbcTemplate jdbcTemplate;

	@PostConstruct
	public void setup() {
		Objects.requireNonNull(jdbcTemplate);
		Objects.requireNonNull(findAllServiceProviders);
		Objects.requireNonNull(findAllServiceIDs);
	}

	@Transactional(readOnly = true)
	public List<String> findAllServiceProviders(final String system) {

		final String tableAddedSql = findAllServiceProviders.replace("$tableName", system);
		log.debug("Query findAllServiceProviders is now [{}]", tableAddedSql);

		final List<String> allServiceProviders = jdbcTemplate.queryForList(tableAddedSql, String.class);

		if (allServiceProviders == null) {
			return Collections.emptyList();
		}

		log.debug("Has found {} Service Providers from table {}", allServiceProviders.size(), system);

		return allServiceProviders;

	}

	@Transactional(readOnly = true)
	public List<String> findAllDepartments(final String system) {

		final String tableAddedSql = findAllDepartments.replace("$tableName", system);
		log.debug("Query findAllDepartments is now [{}]", tableAddedSql);

		final List<String> allDepartments = jdbcTemplate.queryForList(tableAddedSql, String.class);

		if (allDepartments == null) {
			return Collections.emptyList();
		}

		log.debug("Has found {} Departments from table {}", allDepartments.size(), system);

		return allDepartments;

	}

	@Transactional(readOnly = true)
	public List<String> findAllServiceIDs(final String system) {

		final String tableAddedSql = findAllServiceIDs.replace("$tableName", system);
		log.debug("Query findAllServiceIDs is now [{}]", tableAddedSql);

		final List<String> allServiceIds = jdbcTemplate.queryForList(tableAddedSql, String.class);

		if (allServiceIds == null) {
			return Collections.emptyList();
		}

		log.debug("Has found {} serviceIDs", allServiceIds.size());

		return allServiceIds;

	}

}
