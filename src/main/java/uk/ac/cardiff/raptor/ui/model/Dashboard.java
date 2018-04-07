package uk.ac.cardiff.raptor.ui.model;

/**
 * Bean for storing results relating to a single serviceID.
 * 
 * @author philsmart
 *
 */

public class Dashboard {

	/**
	 * Stores graph information for producing visual outputs
	 */
	private final DashboardGraphs graphs = new DashboardGraphs();

	/**
	 * Stores scaler values for display
	 */
	private final DashboardScalers scalers = new DashboardScalers();

	/**
	 * Stores the corresponding tabular information to the graph information
	 */
	private final DashboardTables tables = new DashboardTables();

	/**
	 * @return the graphs
	 */
	public final DashboardGraphs getGraphs() {
		return graphs;
	}

	/**
	 * @return the scalers
	 */
	public final DashboardScalers getScalers() {
		return scalers;
	}

	/**
	 * @return the tables
	 */
	public final DashboardTables getTables() {
		return tables;
	}

}
