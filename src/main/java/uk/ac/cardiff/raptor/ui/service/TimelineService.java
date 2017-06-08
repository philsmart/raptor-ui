package uk.ac.cardiff.raptor.ui.service;

import org.primefaces.model.timeline.TimelineEvent;
import org.primefaces.model.timeline.TimelineModel;
import org.springframework.stereotype.Service;

import uk.ac.cardiff.raptor.ui.model.chart.TraceRow;
import uk.ac.cardiff.raptor.ui.model.chart.TraceRows;

@Service
public class TimelineService {

	public TimelineModel createTimeline(final TraceRows trace) {
		final TimelineModel model = new TimelineModel();

		for (final TraceRow row : trace.getRows()) {
			model.add(new TimelineEvent(row.getResourceId(), row.getEventTime()));
		}

		return model;
	}

}
