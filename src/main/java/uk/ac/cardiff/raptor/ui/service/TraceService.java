package uk.ac.cardiff.raptor.ui.service;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import uk.ac.cardiff.raptor.ui.jdbc.AuthenticationRepository;
import uk.ac.cardiff.raptor.ui.model.SystemSelection;
import uk.ac.cardiff.raptor.ui.model.Trace;
import uk.ac.cardiff.raptor.ui.model.chart.TraceRows;

/**
 * Service that invokes functions to trace individual user authentications
 * 
 * @author philsmart
 *
 */
@Component
public class TraceService {

	private static final Logger log = LoggerFactory.getLogger(TraceService.class);

	@Inject
	private AuthenticationRepository authRepository;

	@Inject
	private SearchToSqlMapper sqlMapper;

	@Inject
	private TimelineService timeLine;

	public void trace(final SystemSelection system, final Trace trace) throws SearchException {
		log.info("Tracing user account [{}]", trace.getSearch());

		final String tableName = sqlMapper.mapToTableName(system)
				.orElseThrow(() -> new SearchException("No system set, one of Shibboleth or Ezproxy expected"));
		log.debug("Tracing using TableName [{}]", tableName);

		final TraceRows rows = authRepository.findLastAuths(trace, tableName);
		log.debug("Trace has found {} rows for search {}", rows.getRows().size(), trace.getSearch());

		trace.setSearchResult(rows);

		trace.setSearchResultsTimeline(timeLine.createTimeline(rows));
	}

}
