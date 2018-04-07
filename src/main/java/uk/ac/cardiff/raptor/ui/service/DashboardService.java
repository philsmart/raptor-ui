package uk.ac.cardiff.raptor.ui.service;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

import uk.ac.cardiff.raptor.ui.jdbc.AuthenticationRepository;
import uk.ac.cardiff.raptor.ui.model.AuthSystem;
import uk.ac.cardiff.raptor.ui.model.Dashboard;
import uk.ac.cardiff.raptor.ui.model.DashboardGraphs.CHART_TYPE;
import uk.ac.cardiff.raptor.ui.model.DashboardTables.TABLE_TYPE;
import uk.ac.cardiff.raptor.ui.model.chart.GroupByResults;
import uk.ac.cardiff.raptor.ui.secure.RaptorUser;
import uk.ac.cardiff.raptor.ui.secure.SecurityContextHelper;
import uk.ac.cardiff.raptor.ui.utils.DateUtils;

/**
 * Service for constructing all dashboard graphs for each serviceID of users
 * currently authenticated (logged in).
 * 
 * @author philsmart
 *
 */
@Service
public class DashboardService {

	private static final Logger log = LoggerFactory.getLogger(DashboardService.class);

	@Inject
	private AuthenticationRepository authRepository;

	@Inject
	private ChartService chartService;

	@Inject
	private TableService tableService;

	@Inject
	private SearchToSqlMapper sqlMapper;

	/**
	 * Spring specific {@link SessionRegistry}, auto-wired from the bean with name
	 * {@code sessionRegistry}
	 */
	@Inject
	private SessionRegistry sessionRegistry;

	/**
	 * <p>
	 * Constructs dashboard graphs for all logged in users
	 * </p>
	 */
	@Scheduled(initialDelay = 5000, fixedDelay = 50000)
	public void constructDashboards() {
		log.debug("Creating dashboards for {} users", sessionRegistry.getAllPrincipals().size());
		for (final Object principal : sessionRegistry.getAllPrincipals()) {

			if (principal instanceof RaptorUser) {
				log.debug(
						"Constructing all dashboard graphs for logged in user [{}], logged in from SAML IdP entityID [{}]",
						((RaptorUser) principal).getUsername(), ((RaptorUser) principal).getIdpEntityId());

				// final List<String> serviceIds = Arrays
				// .asList(((RaptorUser) principal).getServiceIdMappings().getServiceIds());
				//
				// try {
				// createTopGraphs(serviceIds);
				// } catch (final DashboardException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
			}
		}
	}

	public Dashboard createTopGraphs(final List<String> userServiceIds) throws DashboardException {

		final Dashboard userDash = new Dashboard();

		log.info("Running CREATE TOP graphs for serviceIds [{}]", userServiceIds);

		for (final AuthSystem system : AuthSystem.values()) {

			final String tableName = sqlMapper.mapToTableName(system);

			log.trace("Searching using TableName [{}]", tableName);

			final GroupByResults topAuthsYear = authRepository
					.findTopServiceProvidersByAuthentications(DateUtils.getStartOfYear(), tableName, userServiceIds);

			final GroupByResults topAuthsDistinct = authRepository
					.findTopServiceProvidersByAuthenticationsDistinctUsers(DateUtils.getStartOfYear(), tableName,
							userServiceIds);

			final GroupByResults authsPerMonthYear = authRepository.findAuthsToAllServiceProvidersByPeriod(
					DateUtils.getStartOfYear(), tableName, "week", userServiceIds);

			userDash.getGraphs().addGraph(CHART_TYPE.TOP5_YEAR, chartService.createPieModel(topAuthsYear, 250, false),
					system);

			userDash.getTables().addTable(TABLE_TYPE.TOP5_YEAR, tableService.createGroupByTable(topAuthsYear), system);

			userDash.getGraphs().addGraph(CHART_TYPE.TOP5DISTINCT_YEAR,
					chartService.createPieModel(topAuthsDistinct, 250, false), system);

			userDash.getTables().addTable(TABLE_TYPE.TOP5DISTINCT_YEAR,
					tableService.createGroupByTable(topAuthsDistinct), system);

			userDash.getGraphs().addGraph(CHART_TYPE.AUTHSPER_YEAR, chartService.createLineModel(authsPerMonthYear),
					system);

		}
		log.debug("Finished creating TOP graphs for serviceIds [{}]", userServiceIds);
		return userDash;
	}

	public void createAuthsCount() throws DashboardException {

		for (final AuthSystem system : AuthSystem.values()) {

			final List<String> userServiceIds = SecurityContextHelper.retrieveRaptorUserAuthorisedServiceIds(system);

			final String tableName = sqlMapper.mapToTableName(system);

			final Long noAuths = authRepository.findAllAuthsToServiceProvider(tableName, DateUtils.getStartOfToday(),
					userServiceIds);
			log.trace("Has {} count for Date >= {} from {}", noAuths, DateUtils.getStartOfToday(), system);

			final Long noAuthsDistinct = authRepository.findAllDistinctAuthsToServiceProvider(tableName,
					DateUtils.getStartOfToday(), userServiceIds);
			log.trace("Has {} count for Date >= {} from {}", noAuthsDistinct, DateUtils.getStartOfToday(), system);

			final Long noAuthsYear = authRepository.findAllAuthsToServiceProvider(tableName, DateUtils.getStartOfYear(),
					userServiceIds);
			log.trace("Has {} count for Date >= {} from {}", noAuthsYear, DateUtils.getStartOfYear(), system);

			final Long noAuthsYearDistinct = authRepository.findAllDistinctAuthsToServiceProvider(tableName,
					DateUtils.getStartOfYear(), userServiceIds);
			log.trace("Has {} count for Date >= {} from {}", noAuthsYearDistinct, DateUtils.getStartOfYear(), system);

			// dashboardScalers.addScaler(DashboardScalers.SCALER_TYPE.AUTHS_TODAY, noAuths,
			// system);

			// dashboardScalers.addScaler(DashboardScalers.SCALER_TYPE.AUTHS_YEAR,
			// noAuthsYear, system);

			// dashboardScalers.addScaler(DashboardScalers.SCALER_TYPE.AUTHS_DISTINCT_TODAY,
			// noAuthsDistinct, system);

			// dashboardScalers.addScaler(DashboardScalers.SCALER_TYPE.AUTHS_DISTINCT_YEAR,
			// noAuthsYearDistinct, system);
		}

	}

	public void createTodayGraphsRT() throws DashboardException {

		for (final AuthSystem system : AuthSystem.values()) {

			final List<String> userServiceIds = SecurityContextHelper.retrieveRaptorUserAuthorisedServiceIds(system);

			final String tableName = sqlMapper.mapToTableName(system);
			log.trace("Searching using TableName [{}]", tableName);

			final GroupByResults topAuthsToday = authRepository
					.findTopServiceProvidersByAuthentications(DateUtils.getStartOfToday(), tableName, userServiceIds);

			final GroupByResults topAuthsDistinct = authRepository
					.findTopServiceProvidersByAuthenticationsDistinctUsers(DateUtils.getStartOfToday(), tableName,
							userServiceIds);

			final GroupByResults authsPerHourDay = authRepository.findAuthsToAllServiceProvidersByPeriod(
					DateUtils.getStartOfToday(), tableName, "hour", userServiceIds);

			// dashboardGraphs.addGraph(CHART_TYPE.TOP5_TODAY,
			// chartService.createPieModel(topAuthsToday, 250, false),
			// system);

			// dashboardTables.addTable(TABLE_TYPE.TOP5_TODAY,
			// tableService.createGroupByTable(topAuthsToday), system);

			// dashboardGraphs.addGraph(CHART_TYPE.TOP5DISTINCT_TODAY,
			// chartService.createPieModel(topAuthsDistinct, 250, false), system);

			// dashboardTables.addTable(TABLE_TYPE.TOP5DISTINCT_TODAY,
			// tableService.createGroupByTable(topAuthsDistinct),
			// system);

			// dashboardGraphs.addGraph(CHART_TYPE.AUTHSPER_TODAY,
			// chartService.createLineModel(authsPerHourDay), system);
		}
	}

}
