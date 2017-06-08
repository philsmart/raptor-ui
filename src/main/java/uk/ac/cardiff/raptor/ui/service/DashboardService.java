package uk.ac.cardiff.raptor.ui.service;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import uk.ac.cardiff.raptor.ui.jdbc.AuthenticationRepository;
import uk.ac.cardiff.raptor.ui.model.DashboardGraphs;
import uk.ac.cardiff.raptor.ui.model.DashboardGraphs.CHART_TYPE;
import uk.ac.cardiff.raptor.ui.model.DashboardScalers;
import uk.ac.cardiff.raptor.ui.model.DashboardTables;
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

	@Inject
	private DashboardScalers dashboardScalers;

	@Scheduled(fixedDelay = 5000000)
	public void createTopGraphs() throws DashboardException {

		log.info("Running CREATE TOP graphs");

		final SystemSelection system = new SystemSelection();
		system.toggleShibboleth();

		final String tableName = sqlMapper.mapToTableName(system)
				.orElseThrow(() -> new DashboardException("No system set, one of Shibboleth or Ezproxy expected"));
		log.debug("Searching using TableName [{}]", tableName);

		final GroupByResults topAuthsShibYear = authRepository
				.findTopServiceProvidersByAuthentications(DateUtils.getStartOfYear(), tableName);

		final GroupByResults topAuthsShibDistinct = authRepository
				.findTopServiceProvidersByAuthenticationsDistinctUsers(DateUtils.getStartOfYear(), tableName);

		final GroupByResults authsPerMonthYear = authRepository
				.findAuthsToAllServiceProvidersByPeriod(DateUtils.getStartOfYear(), tableName, "week");

		dashboardGraphs.getShibCharts().put(CHART_TYPE.TOP5_YEAR, chartService.createPieModel(topAuthsShibYear));

		dashboardGraphs.getShibCharts().put(CHART_TYPE.TOP5DISTINCT_YEAR,
				chartService.createPieModel(topAuthsShibDistinct));

		dashboardGraphs.getShibCharts().put(CHART_TYPE.AUTHSPER_YEAR, chartService.createLineModel(authsPerMonthYear));
	}

	@Scheduled(fixedDelay = 10000)
	public void createAuthsCount() throws DashboardException {
		final SystemSelection system = new SystemSelection();
		system.toggleShibboleth();

		final String tableName = sqlMapper.mapToTableName(system)
				.orElseThrow(() -> new DashboardException("No system set, one of Shibboleth or Ezproxy expected"));
		log.debug("Searching using TableName [{}]", tableName);

		final Long noAuths = authRepository.findAllAuthsToServiceProvider(tableName, DateUtils.getStartOfToday());
		log.trace("Has {} count for Date >= {} from Shibboleth", noAuths, DateUtils.getStartOfToday());

		dashboardScalers.addScaler(DashboardScalers.SCALER_TYPE.AUTHS_TODAY, noAuths,
				SystemSelection.SYSTEM.SHIBBOLETH);
	}

	@Scheduled(fixedDelay = 100000)
	public void createTodayGraphsRT() throws DashboardException {

		final SystemSelection system = new SystemSelection();
		system.toggleShibboleth();

		final String tableName = sqlMapper.mapToTableName(system)
				.orElseThrow(() -> new DashboardException("No system set, one of Shibboleth or Ezproxy expected"));
		log.debug("Searching using TableName [{}]", tableName);

		final GroupByResults topAuthsShibToday = authRepository
				.findTopServiceProvidersByAuthentications(DateUtils.getStartOfToday(), tableName);

		final GroupByResults topAuthsShibDistinct = authRepository
				.findTopServiceProvidersByAuthenticationsDistinctUsers(DateUtils.getStartOfToday(), tableName);

		final GroupByResults authsPerHourDay = authRepository
				.findAuthsToAllServiceProvidersByPeriod(DateUtils.getStartOfToday(), tableName, "hour");

		dashboardGraphs.getShibCharts().put(CHART_TYPE.TOP5_TODAY, chartService.createPieModel(topAuthsShibToday));

		dashboardGraphs.getShibCharts().put(CHART_TYPE.TOP5DISTINCT_TODAY,
				chartService.createPieModel(topAuthsShibDistinct));

		dashboardGraphs.getShibCharts().put(CHART_TYPE.AUTHSPER_TODAY, chartService.createLineModel(authsPerHourDay));

	}

}
