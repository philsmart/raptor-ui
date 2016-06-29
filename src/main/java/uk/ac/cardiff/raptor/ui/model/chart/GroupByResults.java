package uk.ac.cardiff.raptor.ui.model.chart;

import java.util.ArrayList;
import java.util.List;

public class GroupByResults {

	private String seriesLabel;

	/**
	 * @return the seriesLabel
	 */
	public final String getSeriesLabel() {
		return seriesLabel;
	}

	/**
	 * @param seriesLabel
	 *            the seriesLabel to set
	 */
	public final void setSeriesLabel(final String seriesLabel) {
		this.seriesLabel = seriesLabel;
	}

	/**
	 * @return the results
	 */
	public List<GroupByResult> getResults() {
		if (results == null) {
			results = new ArrayList<GroupByResult>();
		}
		return results;
	}

	/**
	 * @param results
	 *            the results to set
	 */
	public void setResults(final List<GroupByResult> results) {
		this.results = results;
	}

	private List<GroupByResult> results;

}
