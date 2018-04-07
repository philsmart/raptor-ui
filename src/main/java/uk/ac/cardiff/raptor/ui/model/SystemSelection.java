package uk.ac.cardiff.raptor.ui.model;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ManagedBean
@SessionScoped
public class SystemSelection {

	private static final Logger log = LoggerFactory.getLogger(SystemSelection.class);

	/**
	 * The value of the currently selected {@link AuthSystem}
	 */
	private AuthSystem selectedAuthSystem;

	/**
	 * The currently selected serviceId
	 */
	private String selectedServiceId;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("SystemSelection [selectedAuthSystem=");
		builder.append(selectedAuthSystem);
		builder.append(", selectedServiceId=");
		builder.append(selectedServiceId);
		builder.append("]");
		return builder.toString();
	}

	/**
	 * Sets the selectedServiceId and the selectedAuthSystem.
	 * 
	 * @param authSystem
	 *            the {@link String} value of the {@link AuthSystem} to set
	 * @param serviceId
	 *            the serviceId to set
	 */
	public void setSelectedAuthSystem(final String authSystem, final String serviceId) {
		log.info("Setting selected authentication system to [{}] with serviceId [{}]", authSystem, serviceId);
		selectedServiceId = serviceId;
		try {
			selectedAuthSystem = AuthSystem.valueOf(authSystem);
		} catch (final IllegalArgumentException e) {
			log.error("Could not set selected auth system, requested type [{}] does not exist in auth types [{}]",
					authSystem, AuthSystem.values(), e);
		}

	}

	public SystemSelection() {

		selectedAuthSystem = AuthSystem.SHIBBOLETH_IDP;

	}

	/**
	 * @return the selected
	 */
	public AuthSystem getSelected() {
		return selectedAuthSystem;
	}

	/**
	 * @param selected
	 *            the selected to set
	 */
	public void setSelected(final AuthSystem selected) {
		this.selectedAuthSystem = selected;
	}

	/**
	 * @return the selectedServiceId
	 */
	public String getSelectedServiceId() {
		return selectedServiceId;
	}

}
