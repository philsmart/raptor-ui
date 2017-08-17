package uk.ac.cardiff.raptor.ui.service;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import uk.ac.cardiff.raptor.ui.model.chart.SearchGraph;

/**
 * Constructs CSV, PDF, reports based on the input {@link SearchGraph} results.
 * 
 * @author philsmart
 *
 */
@Component
public class ReportConstructorService {

	private static final Logger log = LoggerFactory.getLogger(ReportConstructorService.class);

	public void constructCsvReport(final SearchGraph search) {
		log.info("Report Constructor creating CSV report for [{}]", search.getTable());

		final String csv = search.getTable().getRows().stream().map(row -> row.getLabel() + "," + row.getValue())
				.collect(Collectors.joining("\n"));

		log.debug("Created CSV as \n{}", csv);
	}

}
