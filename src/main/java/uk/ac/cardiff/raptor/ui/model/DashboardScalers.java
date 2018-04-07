package uk.ac.cardiff.raptor.ui.model;

import java.util.HashMap;
import java.util.Map;

/**
 * For storing results with single values e.g. counts.
 * 
 * @author philsmart
 *
 */
public class DashboardScalers {

	public static enum SCALER_TYPE {
		AUTHS_TODAY, AUTHS_YEAR, AUTHS_DISTINCT_TODAY, AUTHS_DISTINCT_YEAR
	}

	private final Map<String, Map<String, Long>> scalers = new HashMap<String, Map<String, Long>>();

	public void addScaler(final SCALER_TYPE type, final Long value, final AuthSystem system) {
		if (scalers.containsKey(system.name())) {
			scalers.get(system.name()).put(type.name(), value);
		} else {
			final Map<String, Long> inner = new HashMap<String, Long>();
			inner.put(type.name(), value);
			scalers.put(system.name(), inner);
		}

	}

	/**
	 * @return the scalers
	 */
	public Map<String, Map<String, Long>> getScalers() {
		return scalers;
	}

}
