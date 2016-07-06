package uk.ac.cardiff.raptor.ui.model;

import java.util.HashMap;
import java.util.Map;

import org.primefaces.model.chart.ChartModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DashboardGraphs {

	private static final Logger log = LoggerFactory.getLogger(DashboardGraphs.class);

	public enum CHART_TYPE {
		TOP5_YEAR,

		TOP5DISTINCT_YEAR,

		AUTHSPER_YEAR,

		TOP5_TODAY,

		TOP5DISTINCT_TODAY,

		AUTHSPER_TODAY
	}

	private Map<CHART_TYPE, ChartModel> shibCharts = new HashMap<CHART_TYPE, ChartModel>();

	private Map<CHART_TYPE, ChartModel> ezproxyCharts = new HashMap<CHART_TYPE, ChartModel>();

	public ChartModel getShibChartsGetter(final String chartType) {
		final CHART_TYPE type = CHART_TYPE.valueOf(chartType);
		return shibCharts.get(type);
	}

	public ChartModel getExproxyChartsGetter(final String chartType) {
		final CHART_TYPE type = CHART_TYPE.valueOf(chartType);
		return ezproxyCharts.get(type);
	}

	/**
	 * @return the shibCharts
	 */
	public final Map<CHART_TYPE, ChartModel> getShibCharts() {

		return shibCharts;
	}

	/**
	 * @param shibCharts
	 *            the shibCharts to set
	 */
	public final void setShibCharts(final Map<CHART_TYPE, ChartModel> shibCharts) {
		this.shibCharts = shibCharts;
	}

	/**
	 * @return the ezproxyCharts
	 */
	public final Map<CHART_TYPE, ChartModel> getEzproxyCharts() {
		return ezproxyCharts;
	}

	/**
	 * @param ezproxyCharts
	 *            the ezproxyCharts to set
	 */
	public final void setEzproxyCharts(final Map<CHART_TYPE, ChartModel> ezproxyCharts) {
		this.ezproxyCharts = ezproxyCharts;
	}

}
