package uk.ac.cardiff.raptor.ui.model.chart;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.primefaces.model.chart.ChartModel;
import org.primefaces.model.chart.HorizontalBarChartModel;

@ManagedBean
@RequestScoped
public class SearchGraph {

	/**
	 * The constructed chart
	 */
	private ChartModel chart;

	public String getChartHeight() {
		if (chart instanceof HorizontalBarChartModel) {
			final HorizontalBarChartModel model = (HorizontalBarChartModel) chart;
			if (model.getSeries() != null) {
				if (model.getSeries().get(0).getData().size() < 20) {
					return "height:500px;";
				}
				if (model.getSeries().get(0).getData().size() < 50) {
					return "height:1200px;";
				}
			}
		}
		return "height:1700px;";
	}

	/**
	 * @return the chart
	 */
	public ChartModel getChart() {
		return chart;
	}

	/**
	 * @param chart
	 *            the chart to set
	 */
	public void setChart(final ChartModel chart) {
		this.chart = chart;
	}

}
