package uk.ac.cardiff.raptor.ui.model;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import uk.ac.cardiff.raptor.ui.model.SystemSelection.SYSTEM;
import uk.ac.cardiff.raptor.ui.model.chart.TableModel;

/**
 * One instance for all users - is fine.
 * 
 * @author philsmart
 *
 */
@Component
public class DashboardTables {

	private static final Logger log = LoggerFactory.getLogger(DashboardTables.class);

	public enum TABLE_TYPE {
		TOP5_YEAR,

		TOP5DISTINCT_YEAR,

		TOP5_TODAY,

		TOP5DISTINCT_TODAY,

		AUTHSPERMONTH_TODAY
	}

	private final Map<String, Map<String, TableModel>> tables = new HashMap<String, Map<String, TableModel>>();

	public void addTable(final TABLE_TYPE type, final TableModel value, final SYSTEM system) {
		if (tables.containsKey(system.name())) {
			tables.get(system.name()).put(type.name(), value);
		} else {
			final Map<String, TableModel> inner = new HashMap<String, TableModel>();
			inner.put(type.name(), value);
			tables.put(system.name(), inner);
		}

	}

	/**
	 * @return the tables
	 */
	public Map<String, Map<String, TableModel>> getTables() {
		return tables;
	}

}
