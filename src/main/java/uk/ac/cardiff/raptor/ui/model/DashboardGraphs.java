package uk.ac.cardiff.raptor.ui.model;

import java.util.HashMap;
import java.util.Map;

import org.primefaces.model.chart.ChartModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private final Map<String, Map<String, ChartModel>> graphs = new HashMap<String, Map<String, ChartModel>>();

	public void addGraph(final CHART_TYPE type, final ChartModel value, final AuthSystem system) {
		if (graphs.containsKey(system.name())) {
			graphs.get(system.name()).put(type.name(), value);
		} else {
			final Map<String, ChartModel> inner = new HashMap<String, ChartModel>();
			inner.put(type.name(), value);
			graphs.put(system.name(), inner);
		}

	}

	/**
	 * @return the graphs
	 */
	public Map<String, Map<String, ChartModel>> getGraphs() {
		return graphs;
	}

}
