package uk.ac.cardiff.raptor.ui.service;

import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import uk.ac.cardiff.raptor.ui.jdbc.AuthenticationRepository;
import uk.ac.cardiff.raptor.ui.model.Search;
import uk.ac.cardiff.raptor.ui.model.SystemSelection;
import uk.ac.cardiff.raptor.ui.model.chart.GroupByResults;
import uk.ac.cardiff.raptor.ui.model.chart.SearchGraph;

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

	public void search(final SystemSelection system, final Search search, final SearchGraph graph)
			throws SearchException {
		log.info("Searching for [{}] [{}]", system, search);

		final String tableName = sqlMapper.mapToTableName(system)
				.orElseThrow(() -> new SearchException("No system set, one of Shibboleth or Ezproxy expected"));
		log.debug("Searching using TableName [{}]", tableName);

		final GroupByResults results = selectRunSearch(search, tableName);

		graph.setChart(chartService.createHorizontalBarModel(results));
		graph.setTable(tableService.createGroupByTable(results));
		graph.setPie(chartService.createPieModel(results, 150, false));
	}

	/**
	 * Selects which type of search to run based on the
	 * {@link Search#isSchool()} {@link Search#isUser()}
	 * {@link Search#isServiceProvider()} booleans being set
	 * 
	 * @param search
	 *            the {@link Search} object
	 * @param tableName
	 *            the name of the table to query
	 * @return a {@link GroupByResults} object with the results of the query
	 */
	private GroupByResults selectRunSearch(final Search search, final String tableName) {
		if (search.isUser()) {
			return authRepository.findUserAuthenticationsGroupBy(search, tableName);
		} else if (search.isSchool()) {
			return authRepository.findSchoolAuthentications(search, tableName);
		} else if (search.isServiceProvider()) {
			return authRepository.findServiceProviderAuthentications(search, tableName);
		} else {
			return new GroupByResults();
		}
	}

}
