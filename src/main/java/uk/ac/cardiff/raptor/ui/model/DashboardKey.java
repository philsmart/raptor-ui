package uk.ac.cardiff.raptor.ui.model;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

/**
 * A dashboard key is the tuple <authsystem,serviceIds>
 * 
 * 
 * NOT USED, could be in the future if we wanted to store dashboard per
 * authsystem and serviceids rather than per user,
 * 
 * @author philsmart
 *
 */
@Deprecated
public class DashboardKey {

	/**
	 * What authSystem this dashboard was made for. Used as business key.
	 */
	private AuthSystem forAuthSystem;

	/**
	 * What serviceIds this dashboard was made for. Used as business key.
	 */
	private List<String> forServiceIds;

	/**
	 * A set of dashboard graphs;
	 */
	private Dashboard dashboard;

	public DashboardKey(@Nonnull final AuthSystem forAuthSystem, @Nonnull final List<String> forServiceIds,
			@Nonnull final Dashboard bashboard) {
		Objects.requireNonNull(forAuthSystem);
		Objects.requireNonNull(forServiceIds);
		Objects.requireNonNull(dashboard);

	}

}
