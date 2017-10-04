package uk.ac.cardiff.raptor.ui.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import uk.ac.cardiff.raptor.ui.model.Search;
import uk.ac.cardiff.raptor.ui.model.Trace;
import uk.ac.cardiff.raptor.ui.model.chart.GroupByResult;
import uk.ac.cardiff.raptor.ui.model.chart.GroupByResults;
import uk.ac.cardiff.raptor.ui.model.chart.TraceRow;
import uk.ac.cardiff.raptor.ui.model.chart.TraceRows;
import uk.ac.cardiff.raptor.ui.utils.JdbcUtils;

@Repository
public class AuthenticationRepository {

	private static final Logger log = LoggerFactory.getLogger(AuthenticationRepository.class);

	@Value("${raptorui.sql.get.user}")
	private String getUserSql;

	@Value("${raptorui.sql.get.school}")
	private String getSchoolSql;

	@Value("${raptorui.sql.get.service-provider}")
	private String getServiceProviderSql;

	@Value("${raptorui.sql.get.top.service-providers.auths}")
	private String topServiceProvidersByAuthenticationsSql;

	@Value("${raptorui.sql.get.top.service-providers.auths.distinct}")
	private String topServiceProvidersByAuthenticationsDistinctUsersSql;

	@Value("${raptorui.sql.get.auths-per}")
	private String authenticationsPerPeriod;

	@Value("${raptorui.sql.get.top.service-providers.auths.count}")
	private String authCount;

	@Value("${raptorui.sql.get.top.service-providers.auths.count.distinct}")
	private String authCountDistinct;

	@Value("${raptorui.sql.get.previous-auths}")
	private String previousAuths;

	@Inject
	private JdbcTemplate jdbcTemplate;

	@PostConstruct
	public void setup() {
		Objects.requireNonNull(jdbcTemplate);
		Objects.requireNonNull(getUserSql);
		Objects.requireNonNull(getSchoolSql);
		Objects.requireNonNull(getServiceProviderSql);
		Objects.requireNonNull(topServiceProvidersByAuthenticationsSql);
		Objects.requireNonNull(authCount);
		Objects.requireNonNull(previousAuths);
		log.info("Has JdbcTemplate {}", jdbcTemplate.getDataSource());
		log.info("User SQL is [{}]", getUserSql);
		log.info("School SQL is [{}]", getSchoolSql);
		log.info("Service-Provider SQL is [{}]", getServiceProviderSql);
		log.info("Top ServiceProviders By Auths [{}]", getServiceProviderSql);
		log.info("Authentication Count [{}]", authCount);
		log.info("Previous Authentications [{}]", previousAuths);

	}

	@Transactional(readOnly = true)
	public GroupByResults findUserAuthenticationsGroupBy(final Search search, final String system) {

		String tableAddedSql = getUserSql.replace("$tableName", system);
		tableAddedSql = tableAddedSql.replace("$groupBy", search.getType());
		log.trace("Query is now [{}]", tableAddedSql);
		try {
			final GroupByResults results = jdbcTemplate.query(tableAddedSql,
					new Object[] { search.getTerm(), search.getFrom(), search.getTo() }, new GroupByResultsExtractor());

			return results;
		} catch (final Throwable e) {
			log.error("Issue getting user authentications", e);
			throw e;
		}

	}

	@Transactional(readOnly = true)
	public GroupByResults findSchoolAuthentications(final Search search, final String system) {
		String tableAddedSql = getSchoolSql.replace("$tableName", system);
		tableAddedSql = tableAddedSql.replace("$groupBy", search.getType());
		log.trace("Query is now [{}]", tableAddedSql);
		try {
			final GroupByResults results = jdbcTemplate.query(tableAddedSql,
					new Object[] { search.getTerm(), search.getFrom(), search.getTo() }, new GroupByResultsExtractor());

			return results;
		} catch (final Throwable e) {
			log.error("Issue getting school authentications", e);
			throw e;
		}
	}

	@Transactional(readOnly = true)
	public GroupByResults findServiceProviderAuthentications(final Search search, final String system) {
		String tableAddedSql = getServiceProviderSql.replace("$tableName", system);
		tableAddedSql = tableAddedSql.replace("$groupBy", search.getType());
		log.trace("Query is now [{}]", tableAddedSql);
		try {
			final GroupByResults results = jdbcTemplate.query(tableAddedSql,
					new Object[] { search.getTerm(), search.getFrom(), search.getTo() }, new GroupByResultsExtractor());

			return results;
		} catch (final Throwable e) {
			log.error("Issue getting serviceprovider authentications", e);
			throw e;
		}
	}

	@Transactional(readOnly = true)
	public GroupByResults findTopServiceProvidersByAuthentications(final Date from, final String system) {
		final String tableAddedSql = topServiceProvidersByAuthenticationsSql.replace("$tableName", system);
		log.trace("Query is now [{}]", tableAddedSql);
		try {
			final GroupByResults results = jdbcTemplate.query(tableAddedSql, new Object[] { from },
					new GroupByResultsExtractor());

			return results;
		} catch (final Throwable e) {
			log.error("Issue getting top serviceproviders by authentications", e);
			throw e;
		}
	}

	@Transactional(readOnly = true)
	public GroupByResults findAuthsToAllServiceProvidersByPeriod(final Date from, final String system,
			final String period) {
		final String tableAddedSql = authenticationsPerPeriod.replace("$tableName", system);
		final String periodAddedSql = tableAddedSql.replace("$period", period);
		log.trace("Query is now [{}]", periodAddedSql);
		try {
			final GroupByResults results = jdbcTemplate.query(periodAddedSql, new Object[] { from },
					new GroupByResultsExtractor());

			return results;
		} catch (final Throwable e) {
			log.error("Issue getting authentications by period", e);
			throw e;
		}
	}

	@Transactional(readOnly = true)
	public GroupByResults findTopServiceProvidersByAuthenticationsDistinctUsers(final Date from, final String system) {
		final String tableAddedSql = topServiceProvidersByAuthenticationsDistinctUsersSql.replace("$tableName", system);
		log.trace("Query is now [{}]", tableAddedSql);
		try {
			final GroupByResults results = jdbcTemplate.query(tableAddedSql, new Object[] { from },
					new GroupByResultsExtractor());

			return results;
		} catch (final Throwable e) {
			log.error("Issue getting top serviceproviders by authentications distinct", e);
			throw e;
		}
	}

	@Transactional(readOnly = true)
	public Long findAllAuthsToServiceProvider(final String system, final Date from) {
		final String tableAddedSql = authCount.replace("$tableName", system);
		log.trace("Query is now [{}]", tableAddedSql);
		try {
			final Long count = jdbcTemplate.queryForObject(tableAddedSql, new Object[] { from }, Long.class);

			return count;
		} catch (final Throwable e) {
			log.error("Issue getting top serviceproviders by authentications distinct", e);
			throw e;
		}
	}

	@Transactional(readOnly = true)
	public Long findAllDistinctAuthsToServiceProvider(final String system, final Date from) {
		final String tableAddedSql = authCountDistinct.replace("$tableName", system);
		log.trace("Query is now [{}]", tableAddedSql);
		try {
			final Long count = jdbcTemplate.queryForObject(tableAddedSql, new Object[] { from }, Long.class);

			return count;
		} catch (final Throwable e) {
			log.error("Issue getting top serviceproviders by authentications distinct", e);
			throw e;
		}
	}

	@Transactional(readOnly = true)
	public TraceRows findLastAuths(final Trace trace, final String system) {
		String tableAddedSql = previousAuths.replace("$tableName", system);
		tableAddedSql = tableAddedSql.replace("$limit", Integer.toString(trace.getAuthDepth()));
		log.trace("Query is now [{}]", tableAddedSql);
		try {
			final TraceRows results = jdbcTemplate.query(tableAddedSql, new Object[] { trace.getSearch() },
					new TraceRowsResultsExtractor());

			return results;
		} catch (final Throwable e) {
			log.error("Issue getting top serviceproviders by authentications distinct", e);
			throw e;
		}

	}

	class GroupByResultsExtractor implements ResultSetExtractor<GroupByResults> {
		@Override
		public GroupByResults extractData(final ResultSet rs) throws SQLException, DataAccessException {

			final GroupByResults results = new GroupByResults();

			final ResultSetMetaData meta = rs.getMetaData();

			results.setSeriesLabel(meta.getColumnName(1));

			while (rs.next()) {
				final GroupByResult rowResult = new GroupByResult();
				log.trace("Row {},{}", rs.getString(1), rs.getString(2));
				rowResult.setFieldName(rs.getString(1));
				final Long countLong = rs.getLong(2);
				rowResult.setCount(countLong);
				results.getResults().add(rowResult);

			}

			return results;
		}
	}

	class TraceRowsResultsExtractor implements ResultSetExtractor<TraceRows> {
		@Override
		public TraceRows extractData(final ResultSet rs) throws SQLException, DataAccessException {

			final TraceRows results = new TraceRows();

			final ResultSetMetaData meta = rs.getMetaData();

			while (rs.next()) {
				final TraceRow row = new TraceRow();
				final Date eventTime = JdbcUtils.safeGetTime(rs, "event_time");
				row.setEventTime(eventTime);

				final String pricipalName = JdbcUtils.safeGetString(rs, "principal_name");
				row.setPrincipalName(pricipalName);

				final String resourceId = JdbcUtils.safeGetString(rs, "resource_id");
				row.setResourceId(resourceId);

				final String school = JdbcUtils.safeGetString(rs, "school");
				row.setSchool(school);

				final String serviceId = JdbcUtils.safeGetString(rs, "service_id");
				row.setServiceId(serviceId);

				final String serviceName = JdbcUtils.safeGetString(rs, "service_name");
				row.setServiceName(serviceName);

				for (int i = 1; i <= meta.getColumnCount(); i++) {
					row.getFields().put(meta.getColumnName(i), rs.getObject(meta.getColumnName(i)));

				}
				log.trace("trace row {}", row);
				results.getRows().add(row);

			}

			return results;
		}
	}

}
