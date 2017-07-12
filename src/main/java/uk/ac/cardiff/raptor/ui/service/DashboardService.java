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
import uk.ac.cardiff.raptor.ui.model.DashboardTables.TABLE_TYPE;
import uk.ac.cardiff.raptor.ui.model.SystemSelection.SYSTEM;
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

		for (final SYSTEM system : SYSTEM.values()) {

			final String tableName = sqlMapper.mapToTableName(system);

			log.debug("Searching using TableName [{}]", tableName);

			final GroupByResults topAuthsYear = authRepository
					.findTopServiceProvidersByAuthentications(DateUtils.getStartOfYear(), tableName);

			final GroupByResults topAuthsDistinct = authRepository
					.findTopServiceProvidersByAuthenticationsDistinctUsers(DateUtils.getStartOfYear(), tableName);

			final GroupByResults authsPerMonthYear = authRepository
					.findAuthsToAllServiceProvidersByPeriod(DateUtils.getStartOfYear(), tableName, "week");

			dashboardGraphs.addGraph(CHART_TYPE.TOP5_YEAR, chartService.createPieModel(topAuthsYear, 250, false),
					system);

			dashboardTables.addTable(TABLE_TYPE.TOP5_YEAR, tableService.createGroupByTable(topAuthsYear), system);

			dashboardGraphs.addGraph(CHART_TYPE.TOP5DISTINCT_YEAR,
					chartService.createPieModel(topAuthsDistinct, 250, false), system);

			dashboardTables.addTable(TABLE_TYPE.TOP5DISTINCT_YEAR,
					tableService.createGroupByTable(topAuthsDistinct), system);

			dashboardGraphs.addGraph(CHART_TYPE.AUTHSPER_YEAR, chartService.createLineModel(authsPerMonthYear), system);
		}
	}

	@Scheduled(fixedDelay = 10000)
	public void createAuthsCount() throws DashboardException {

		for (final SYSTEM system : SYSTEM.values()) {

			final String tableName = sqlMapper.mapToTableName(system);

			final Long noAuths = authRepository.findAllAuthsToServiceProvider(tableName, DateUtils.getStartOfToday());
			log.trace("Has {} count for Date >= {} from {}", noAuths, DateUtils.getStartOfToday(), system);

			dashboardScalers.addScaler(DashboardScalers.SCALER_TYPE.AUTHS_TODAY, noAuths, system);
		}

	}

	@Scheduled(fixedDelay = 100000)
	public void createTodayGraphsRT() throws DashboardException {

		for (final SYSTEM system : SYSTEM.values()) {

			final String tableName = sqlMapper.mapToTableName(system);
			log.debug("Searching using TableName [{}]", tableName);

			final GroupByResults topAuthsToday = authRepository
					.findTopServiceProvidersByAuthentications(DateUtils.getStartOfToday(), tableName);

			final GroupByResults topAuthsDistinct = authRepository
					.findTopServiceProvidersByAuthenticationsDistinctUsers(DateUtils.getStartOfToday(), tableName);

			final GroupByResults authsPerHourDay = authRepository
					.findAuthsToAllServiceProvidersByPeriod(DateUtils.getStartOfToday(), tableName, "hour");

			dashboardGraphs.addGraph(CHART_TYPE.TOP5_TODAY, chartService.createPieModel(topAuthsToday, 250, false),
					system);

			dashboardTables.addTable(TABLE_TYPE.TOP5_TODAY, tableService.createGroupByTable(topAuthsToday), system);

			dashboardGraphs.addGraph(CHART_TYPE.TOP5DISTINCT_TODAY,
					chartService.createPieModel(topAuthsDistinct, 250, false), system);

			dashboardTables.addTable(TABLE_TYPE.TOP5DISTINCT_TODAY,
					tableService.createGroupByTable(topAuthsDistinct), system);

			dashboardGraphs.addGraph(CHART_TYPE.AUTHSPER_TODAY, chartService.createLineModel(authsPerHourDay), system);
		}
	}

}
