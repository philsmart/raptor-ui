package uk.ac.cardiff.raptor.ui.model.chart;

import java.util.ArrayList;
import java.util.List;

public class TraceRows {

	private List<TraceRow> rows;

	/**
	 * @return the rows
	 */
	public List<TraceRow> getRows() {
		if (rows == null) {
			rows = new ArrayList<TraceRow>();
		}
		return rows;
	}

	/**
	 * @param rows
	 *            the rows to set
	 */
	public void setRows(final List<TraceRow> rows) {
		this.rows = rows;
	}

}
