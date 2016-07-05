package uk.ac.cardiff.raptor.ui.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import uk.ac.cardiff.raptor.ui.model.chart.GroupByResult;
import uk.ac.cardiff.raptor.ui.model.chart.GroupByResults;
import uk.ac.cardiff.raptor.ui.model.chart.Row;
import uk.ac.cardiff.raptor.ui.model.chart.TableModel;

@Component
public class TableService {

	public TableModel createGroupByTable(final GroupByResults results) {

		final TableModel model = new TableModel();

		final List<Row> rows = new ArrayList<Row>();

		for (final GroupByResult result : results.getResults()) {
			final Row row = new Row();
			row.setLabel(result.getFieldName());
			row.setValue(result.getCount());
			rows.add(row);
		}

		model.setRows(rows);

		return model;

	}

}
