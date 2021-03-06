package uk.ac.cardiff.raptor.ui.model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import uk.ac.cardiff.raptor.ui.model.SystemSelection.SYSTEM;

/**
 * For storing results with single values e.g. counts.
 * 
 * @author philsmart
 *
 */
@Component
public class DashboardScalers {

	public static enum SCALER_TYPE {
		AUTHS_TODAY
	}

	private final Map<String, Map<String, Long>> scalers = new HashMap<String, Map<String, Long>>();

	public void addScaler(final SCALER_TYPE type, final Long value, final SYSTEM system) {
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
