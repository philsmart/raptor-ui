package uk.ac.cardiff.raptor.ui.model;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import uk.ac.cardiff.raptor.ui.model.DashboardGraphs.CHART_TYPE;
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
		TOP5,

		TOP5DISTINCT,
	}

	private Map<TABLE_TYPE, TableModel> shibTables = new HashMap<TABLE_TYPE, TableModel>();

	private Map<TABLE_TYPE, TableModel> ezproxyTables = new HashMap<TABLE_TYPE, TableModel>();

	public TableModel getShibTableGetter(final String chartType) {
		final CHART_TYPE type = CHART_TYPE.valueOf(chartType);
		return shibTables.get(type);
	}

	public TableModel getExproxyTableGetter(final String chartType) {
		final CHART_TYPE type = CHART_TYPE.valueOf(chartType);
		return ezproxyTables.get(type);
	}

	/**
	 * @return the shibTables
	 */
	public final Map<TABLE_TYPE, TableModel> getShibTables() {
		return shibTables;
	}

	/**
	 * @param shibTables
	 *            the shibTables to set
	 */
	public final void setShibTables(final Map<TABLE_TYPE, TableModel> shibTables) {
		this.shibTables = shibTables;
	}

	/**
	 * @return the ezproxyCharts
	 */
	public final Map<TABLE_TYPE, TableModel> getEzproxyTables() {
		return ezproxyTables;
	}

	/**
	 * @param ezproxyCharts
	 *            the ezproxyCharts to set
	 */
	public final void setEzproxyTables(final Map<TABLE_TYPE, TableModel> ezproxyTables) {
		this.ezproxyTables = ezproxyTables;
	}

}
