package uk.ac.cardiff.raptor.ui.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.cardiff.raptor.ui.model.account.ServiceIDAuthZMapping;
import uk.ac.cardiff.raptor.ui.secure.RaptorUser;
import uk.ac.cardiff.raptor.ui.secure.SecurityContextHelper;
import uk.ac.cardiff.raptor.ui.utils.JsfBeanHelper;

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

	// Session scoped, so will have a valid user before this bean is constructed
	public SystemSelection() {
		selectedAuthSystem = AuthSystem.SHIBBOLETH_IDP;
		final Optional<RaptorUser> user = SecurityContextHelper.retrieveRaptorUser();
		if (user.isPresent()) {
			final ServiceIDAuthZMapping mappings = user.get().getServiceIdMappings();
			for (final Map.Entry<String, List<String>> mapping : mappings.getSystemToServiceIdMapping().entrySet()) {
				if (mapping.getValue().contains(user.get().getIdpEntityId())) {
					// assume they are allowed to see it, and it will match with the menu displayed
					// on the page
					selectedServiceId = user.get().getIdpEntityId();
				}

			}

		}

	}

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
	 * Sets the selectedServiceId and the selectedAuthSystem. Re-constructs the
	 * dynamic menu modal instance for this request by calling
	 * {@link DynamicMenuModel#createMenuModel()} method on the bean obtained from
	 * the {@link FacesContext}
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
		// now update the menu modal. TODO maybe not the best place for this, but makes
		// it work for whichever view calls this method.
		final DynamicMenuModel selectedSystem = JsfBeanHelper.findBean("dynamicMenuModel");
		selectedSystem.createMenuModel();

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
