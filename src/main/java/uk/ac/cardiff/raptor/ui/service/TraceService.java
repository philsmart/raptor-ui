package uk.ac.cardiff.raptor.ui.service;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import uk.ac.cardiff.raptor.ui.jdbc.AuthenticationRepository;
import uk.ac.cardiff.raptor.ui.model.SystemSelection;
import uk.ac.cardiff.raptor.ui.model.Trace;
import uk.ac.cardiff.raptor.ui.model.chart.TraceRows;
import uk.ac.cardiff.raptor.ui.secure.SecurityContextHelper;

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

		final List<String> userServiceIds = SecurityContextHelper
				.retrieveRaptorUserAuthorisedServiceIds(system.getSelected());

		final String tableName = sqlMapper.mapToTableName(system)
				.orElseThrow(() -> new SearchException("No system set, one of Shibboleth or Ezproxy expected"));
		log.debug("Tracing using TableName [{}]", tableName);

		final TraceRows rows = authRepository.findLastAuths(trace, tableName, userServiceIds);
		log.debug("Trace has found {} rows for search {}", rows.getRows().size(), trace.getSearch());

		trace.setSearchResult(rows);

		trace.setSearchResultsTimeline(timeLine.createTimeline(rows));
	}

}
