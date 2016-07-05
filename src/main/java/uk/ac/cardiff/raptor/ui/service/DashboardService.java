package uk.ac.cardiff.raptor.ui.service;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import uk.ac.cardiff.raptor.ui.jdbc.AuthenticationRepository;
import uk.ac.cardiff.raptor.ui.model.DashboardGraphs;
import uk.ac.cardiff.raptor.ui.model.DashboardGraphs.CHART_TYPE;
import uk.ac.cardiff.raptor.ui.model.DashboardTables;
import uk.ac.cardiff.raptor.ui.model.DashboardTables.TABLE_TYPE;
import uk.ac.cardiff.raptor.ui.model.SystemSelection;
import uk.ac.cardiff.raptor.ui.model.chart.GroupByResults;
import uk.ac.cardiff.raptor.ui.utils.DateUtils;

@Component
public class DashboardService {

	private static final Logger log = LoggerFactory.getLogger(SearchService.class);

	@Inject
	private AuthenticationRepository authRepository;

	@Inject
	private ChartService chartService;

	@Inject
	private TableService tableService;

	@Inject
	private SearchToSqlMapper sqlMapper;

	@Inject
	private DashboardTables dashboardTables;

	@Inject
	private DashboardGraphs dashboardGraphs;

	@Scheduled(fixedDelay = 50000)
	public void createTopGraphs() throws DashboardException {

		log.info("Running CREATE TOP graphs");

		final SystemSelection system = new SystemSelection();
		system.toggleShibboleth();

		final String tableName = sqlMapper.mapToTableName(system)
				.orElseThrow(() -> new DashboardException("No system set, one of Shibboleth or Ezproxy expected"));
		log.debug("Searching using TableName [{}]", tableName);

		final GroupByResults topAuthsShib = authRepository
				.findTopServiceProvidersByAuthentications(DateUtils.getStartOfYear(), tableName);
		// final GroupByResults topAuthsShibDistinct = authRepository
		// .findTopServiceProvidersByAuthenticationsDistinctUsers(tableName);

		final GroupByResults authsPerMonthYear = authRepository
				.findAuthsToAllServiceProvidersByPeriod(DateUtils.getStartOfYear(), tableName, "week");

		dashboardTables.getShibTables().put(TABLE_TYPE.TOP5, tableService.createGroupByTable(topAuthsShib));
		dashboardGraphs.getShibCharts().put(CHART_TYPE.TOP5, chartService.createPieModel(topAuthsShib));
		// dashboardGraphs.getShibCharts().put(CHART_TYPE.TOP5DISTINCT,
		// chartService.createPieModel(topAuthsShibDistinct));
		dashboardGraphs.getShibCharts().put(CHART_TYPE.AUTHSPERMONTH_YEAR,
				chartService.createLineModel(authsPerMonthYear));
	}

}
