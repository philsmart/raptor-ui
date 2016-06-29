package uk.ac.cardiff.raptor.ui.service;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;
import org.primefaces.model.chart.LineChartModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import uk.ac.cardiff.raptor.ui.model.chart.GroupByResult;
import uk.ac.cardiff.raptor.ui.model.chart.GroupByResults;

@Component
public class ChartService {

	private static final Logger log = LoggerFactory.getLogger(ChartService.class);

	public LineChartModel createLineModel(final GroupByResults results) {

		final LineChartModel model = new LineChartModel();

		return model;

	}

	public HorizontalBarChartModel createHorizontalBarModel(final GroupByResults results) {

		final HorizontalBarChartModel model = new HorizontalBarChartModel();

		if (results == null) {
			return model;
		}

		final ChartSeries series = new ChartSeries();

		series.setLabel(results.getSeriesLabel());
		final Axis xAxis = model.getAxis(AxisType.X);
		xAxis.setLabel("Authentications");

		final Axis yAxis = model.getAxis(AxisType.Y);
		yAxis.setLabel(results.getSeriesLabel());

		for (final GroupByResult result : results.getResults()) {
			log.debug("Creating graph from result {}", result);
			series.set(result.getFieldName(), result.getCount());
		}

		model.addSeries(series);

		return model;

	}

}
