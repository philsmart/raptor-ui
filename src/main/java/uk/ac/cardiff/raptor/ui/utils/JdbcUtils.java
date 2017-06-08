package uk.ac.cardiff.raptor.ui.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdbcUtils {

	private static final Logger log = LoggerFactory.getLogger(JdbcUtils.class);

	public static Date safeGetDate(final ResultSet rs, final String columnName) {
		try {
			final Date dateObjct = rs.getDate(columnName);
			return dateObjct;
		} catch (final SQLException e) {
			log.error("Could not retrieve Date column {},{}", columnName, e.getMessage());
		}
		return null;
	}

	public static Date safeGetTime(final ResultSet rs, final String columnName) {
		try {
			final Date dateObjct = rs.getTimestamp(columnName);
			return dateObjct;
		} catch (final SQLException e) {
			log.error("Could not retrieve Date column {},{}", columnName, e.getMessage());
		}
		return null;
	}

	public static String safeGetString(final ResultSet rs, final String columnName) {
		try {
			final String value = rs.getString(columnName);
			return value;
		} catch (final SQLException e) {
			log.error("Could not retrieve String column {},{}", columnName, e.getMessage());
		}
		return null;
	}

}
