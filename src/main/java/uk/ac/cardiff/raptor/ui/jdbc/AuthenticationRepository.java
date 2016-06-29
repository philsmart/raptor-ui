package uk.ac.cardiff.raptor.ui.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
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
import uk.ac.cardiff.raptor.ui.model.chart.GroupByResult;
import uk.ac.cardiff.raptor.ui.model.chart.GroupByResults;

@Repository
public class AuthenticationRepository {

	private static final Logger log = LoggerFactory.getLogger(AuthenticationRepository.class);

	@Value("${raptorui.sql.get.user}")
	private String getUserSql;

	@Value("${raptorui.sql.get.school}")
	private String getSchoolSql;

	@Value("${raptorui.sql.get.service-provider}")
	private String getServiceProviderSql;

	@Inject
	private JdbcTemplate jdbcTemplate;

	@PostConstruct
	public void setup() {
		Objects.requireNonNull(jdbcTemplate);
		Objects.requireNonNull(getUserSql);
		Objects.requireNonNull(getSchoolSql);
		Objects.requireNonNull(getServiceProviderSql);
		log.info("Has JdbcTemplate {}", jdbcTemplate.getDataSource());
		log.info("User SQL is [{}]", getUserSql);
		log.info("School SQL is [{}]", getSchoolSql);
		log.info("Service-Provider SQL is [{}]", getServiceProviderSql);

	}

	@Transactional(readOnly = true)
	public GroupByResults findUserAuthenticationsGroupBy(final Search search, final String system) {

		final String tableAddedSql = getUserSql.replace("$tableName", system);
		log.debug("Query is now [{}]", tableAddedSql);
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
		final String tableAddedSql = getSchoolSql.replace("$tableName", system);
		log.debug("Query is now [{}]", tableAddedSql);
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
		final String tableAddedSql = getServiceProviderSql.replace("$tableName", system);
		log.debug("Query is now [{}]", tableAddedSql);
		try {
			final GroupByResults results = jdbcTemplate.query(tableAddedSql,
					new Object[] { search.getTerm(), search.getFrom(), search.getTo() }, new GroupByResultsExtractor());

			return results;
		} catch (final Throwable e) {
			log.error("Issue getting serviceprovider authentications", e);
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
				log.debug("Row {},{}", rs.getString(1), rs.getString(2));
				rowResult.setFieldName(rs.getString(1));
				final Long countLong = rs.getLong(2);
				rowResult.setCount(countLong);
				results.getResults().add(rowResult);

			}

			return results;
		}
	}

}
