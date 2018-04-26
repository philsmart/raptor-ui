package uk.ac.cardiff.raptor.ui.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import uk.ac.cardiff.raptor.ui.jdbc.AuthenticationRepository;
import uk.ac.cardiff.raptor.ui.model.AuthSystem;
import uk.ac.cardiff.raptor.ui.model.Search;
import uk.ac.cardiff.raptor.ui.model.SystemSelection;
import uk.ac.cardiff.raptor.ui.model.chart.GroupByResults;
import uk.ac.cardiff.raptor.ui.model.chart.SearchGraph;
import uk.ac.cardiff.raptor.ui.secure.SecurityContextHelper;

/**
 * Use by all clients, must be threadsafe.
 * 
 * @author philsmart
 *
 */
@Component
public class SearchService {

	private static final Logger log = LoggerFactory.getLogger(SearchService.class);

	@Inject
	private AuthenticationRepository authRepository;

	@Inject
	private ChartService chartService;

	@Inject
	private TableService tableService;

	@Inject
	private SearchToSqlMapper sqlMapper;

	@PostConstruct
	public void init() {
		Objects.requireNonNull(authRepository);
		Objects.requireNonNull(chartService);
	}

	// TODO require a security check here such that the system selection is
	// authorised for that user, otherwise a POST request could be faked
	public void search(final SystemSelection system, final Search search, final SearchGraph graph)
			throws SearchException {
		log.info("Searching for [{}] [{}]", system, search);

		final String tableName = sqlMapper.mapToTableName(system)
				.orElseThrow(() -> new SearchException("No system set, one of Shibboleth or Ezproxy expected"));
		log.trace("Searching using TableName [{}]", tableName);

		final GroupByResults results = selectRunSearch(search, tableName, system);

		graph.setChart(chartService.createHorizontalBarModel(results));
		graph.setTable(tableService.createGroupByTable(results));
		graph.setPie(chartService.createPieModel(results, 150, false));
	}

	/**
	 * Selects which type of search to run based on the {@link Search#isSchool()}
	 * {@link Search#isUser()} {@link Search#isServiceProvider()} booleans being
	 * set.
	 * 
	 * <p>
	 * The relevant search method is then called. These are:
	 * <ul>
	 * <li>{@link AuthenticationRepository#findUserAuthenticationsGroupBy} iff
	 * {@link Search#isUser()}</li>
	 * <li>{@link AuthenticationRepository#findServiceProviderAuthentications} iff
	 * {@link Search#isServiceProvider()}</li>
	 * <li>{@link AuthenticationRepository#findSchoolAuthentications} iff
	 * {@link Search#isSchool()}</li>
	 * </ul>
	 * .
	 * </p>
	 * 
	 * @param search
	 *            the {@link Search} object
	 * @param tableName
	 *            the name of the authentication table to query
	 * @param system
	 *            the {@link SystemSelection} with knowledge of the selected service
	 *            IDs and {@link AuthSystem}
	 * @return a {@link GroupByResults} object with the results of the query
	 */
	private GroupByResults selectRunSearch(final Search search, final String tableName,
			final SystemSelection selection) {

		final List<String> userServiceIds = new ArrayList<>();
		userServiceIds.add(selection.getSelectedServiceId());

		SecurityContextHelper.checkUserRights(userServiceIds, selection.getSelected().name());

		if (search.isUser()) {
			return authRepository.findUserAuthenticationsGroupBy(search, tableName, userServiceIds);
		} else if (search.isSchool()) {
			return authRepository.findSchoolAuthentications(search, tableName, userServiceIds);
		} else if (search.isServiceProvider()) {
			return authRepository.findServiceProviderAuthentications(search, tableName, userServiceIds);
		} else {
			return new GroupByResults();
		}

	}

}
